package com.molamil.osonegro;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.molamil.osonegro.context.BlockContext;
import com.molamil.osonegro.context.CommandContext;
import com.molamil.osonegro.context.PageContext;
import com.molamil.osonegro.context.ViewableContext;
import com.molamil.osonegro.factory.ObjectFactory;
import com.molamil.osonegro.manager.AbstractStateManager;
import com.molamil.osonegro.manager.State;
import com.molamil.osonegro.utils.Logger;
import com.molamil.osonegro.utils.ObjectUtil;

public class OsoNegroApp implements OnClickListener {
	
	public static final String STATE_CHANGE = "stateChange";
	public static final String PAGE_REQUEST = "pageRequest";
	
	private Map<String, Object> pages;
	private Map<String, Object> blocks;
	private Map<String, Object> commands;
	private Map<String, ArrayList<Handler>> handlers;
	private ObjectFactory factory;
	private PageContext currentPageContext;
	public PageContext getCurrentPageContext() {
		return currentPageContext;
	}


	private PageContext nextPageContext;
	private Map<String, ViewGroup> containers;
	private View currentPage;


	private View nextPage;
	private ArrayList<String> curBlockIds;
	
	private ViewGroup rootView;
	private static Map<String,Object> props;
	private static Activity _activity;
	
	static public Activity getAndroidActivity() {
		return _activity;
	}
	
	public Object getProp(String key) {
		return props.get(key);
	}

	public OsoNegroApp(ViewGroup rootView, Activity activity) {
		this.rootView = rootView;
		_activity = activity;
	}
	
	public void pause() {

	}
	
	public void resume() {

	}
	public void init(InputStream configuration) throws XmlPullParserException, IOException {

	    containers = new HashMap<String, ViewGroup>();
	    
	    curBlockIds = new ArrayList<String>();
	    
	    // LOAD CONFIGURATION
		OsoNegroParser parser = new OsoNegroParser();
		Map<String, Map<String,Object>> conf = parser.parse(configuration);
		pages = conf.get("pages");
		blocks = conf.get("blocks");
		commands = conf.get("commands");
		props = conf.get("props");
	    
	    // EXTRACT EVENTS FROM PAGES
	    extractHandlers();

	    factory = new ObjectFactory();
	    factory.initWithObjects(pages, blocks, commands, props);
	    
	    NotificationCenter.defaultCenter().addObserver(this, "stateChange", STATE_CHANGE);
	}
	
	
	public PageContext gotoPageWithIdAndData(String pageId, Object data, PageContext pcontext) throws Exception {
		
		PageContext context = pcontext != null ? pcontext : factory.getPageContextWithId(pageId);
		
		if(context == null) {
			Logger.error("Page context for " + pageId + " is not found.");
			return null;
		}

		if(currentPageContext != null && (currentPageContext.getId().equals(pageId) || currentPageContext == context)) {
			ObjectUtil.mergePropsWithObject(data, currentPageContext.getMaster().getTarget());
			return context;
		}
		
		if(nextPageContext == context) {
			ObjectUtil.mergePropsWithObject(data, nextPageContext.getMaster().getTarget());
			return context;
		}
		
		NotificationCenter.defaultCenter().postNotification(PAGE_REQUEST, context);
		context.setContainer(resolveContainerForContext(context));
		
		context.getMaster().display();
		ObjectUtil.mergePropsWithObject(data, context.getMaster().getTarget());
		
		if(currentPageContext != null) {
			nextPageContext = context;
			context.getManager().setState(AbstractStateManager.PREV_STATE_OUT);
			currentPageContext.getManager().setState(AbstractStateManager.STATE_OUT);
			String[] depends = currentPageContext.getDepends(); 
			if(depends != null) {
				for(int i = 0; i < depends.length; i++) {
					clearBlockWithId(depends[i]);
				}
			}
		} else {
			context.getManager().setState(AbstractStateManager.STATE_IN);
			currentPageContext = context;
			
			try {
				context.getMaster().getTarget().getClass().getMethod("setup").invoke(context.getMaster().getTarget());
			} catch (NoSuchMethodException e) {
				Logger.debug("No setup method on " + context.getMaster().getTarget());
			}
		}
		
		if (context.getDepends() != null) {
			String[] depends = context.getDepends();
			for(int i = 0; i < depends.length; i++) {
				Logger.debug("Page " + pageId + " depends on " + depends[i]);
				displayBlockWithIdAndData(depends[i], null, null);
			}
		}
		
		
		return context;
	}


	public BlockContext displayBlockWithIdAndData(String blockId, Object data, BlockContext bcontext) throws Exception {
    
		Logger.debug("displayBlockWithId: "+blockId);
    
		// Look up the page context.
		BlockContext context = bcontext != null ? bcontext : factory.getBlockContextWithId(blockId);
    

		if(context == null) {
			Logger.error("Block context for "+blockId+" is not found");
			return context;
		}

		int index = curBlockIds.indexOf(blockId);
    
		if(index > -1) {
			ObjectUtil.mergePropsWithObject(data, context.getMaster().getTarget());
			return null;
		}
    
		curBlockIds.add(blockId);

    
		context.setContainer(resolveContainerForContext(context));
    
		// display page
		context.getMaster().display();

		ObjectUtil.mergePropsWithObject(data, context.getMaster().getTarget());

		// execute setup
		try {
			context.getMaster().getTarget().getClass().getMethod("setup").invoke(context.getMaster().getTarget());
		} catch (NoSuchMethodException e) {
			Logger.debug("No setup method on " + context.getMaster().getTarget());
		}
    
		// set STATE_IN
		context.getManager().setState(AbstractStateManager.STATE_IN);
    
		return context;
	}
	
	
	public void clearBlockWithId(String blockId) throws Exception {
		Logger.debug("clearBlockWithId: " + blockId);
	    BlockContext context = factory.getBlockContextWithId(blockId);
	    int index = curBlockIds.indexOf(blockId);
	    if(index > -1) {
	    	context.getManager().setState(AbstractStateManager.STATE_OUT);
	    } else {
	    	Logger.debug("Block with id " + blockId + " not found for clearing.");
	    }
	}
	
	public void clearPageWithId(String pageId) throws Exception {
	    PageContext context = factory.getPageContextWithId(pageId);
	    context.getMaster().clear();
	}
	
	public CommandContext executeCommandWithIdAndData(String commandId, Object data, CommandContext ccontext) throws Exception {

	    CommandContext context = ccontext != null ? ccontext : factory.getCommandContextWithId(commandId);
	    
	    if(context == null) {
	        Logger.error("Command context for " + commandId + " is not found");
	        return null;
	    }
	    
	    Object command = context.getMaster().build();
	    
	    ObjectUtil.mergePropsWithObject(data, command);
	    
	    context.getMaster().execute();
	    
	    return context;
	}

	
	public View addContainerWithName(ViewGroup view, String name, boolean block) {
		containers.put(name, view);
		if(block) {
			view.setOnClickListener(this);
		}
	    return view;
	}
	
	
	
	public void stateChange(Notification note) throws Exception {
	    
	    State state = (State) note.getData();
	    if(currentPageContext != null && currentPageContext.getManager().getTarget() == state.getTarget()) {
	    	if(state.getState().equals(AbstractStateManager.STATE_OFF)) {
	    		
	    		// execute destroy
	    		try {
	    			currentPageContext.getMaster().getTarget().getClass().getMethod("destroy").invoke(currentPageContext.getMaster().getTarget());
	    		} catch (NoSuchMethodException e) {
	    			Logger.debug("No destroy method on " + currentPageContext.getMaster().getTarget());
	    		}
	    		
	    		currentPageContext.getMaster().clear();
	            currentPageContext = nextPageContext;
	            currentPageContext.getManager().setState(AbstractStateManager.STATE_IN);

	            // execute setup
	            try {
    				currentPageContext.getMaster().getTarget().getClass().getMethod("setup").invoke(currentPageContext.getMaster().getTarget());
	    		} catch (NoSuchMethodException e) {
	    			Logger.debug("No setup method on " + currentPageContext.getMaster().getTarget());
	    		}
	        }
	    } else if(state.getState().equals(AbstractStateManager.STATE_OFF)) {
	        for(int i = 0; i < curBlockIds.size(); i++) {
	            String blockId = curBlockIds.get(i);
	            BlockContext context = factory.getBlockContextWithId(blockId);
	            
	            if(context.getManager().getTarget() == state.getTarget()) {
	            	curBlockIds.remove(i);
	            	context.getMaster().clear();
	                break;
	            }
	        }
	    }
	    
	    hideUnusedContainers();    
	}
	
	public void onReceive(Notification note) throws Exception {
		
		String type = note.getType();
		
/*		if(type.equals(STATE_CHANGE)) {
			stateChange(intent);
			return;
		}*/

	    Object data = note.getData();
	    
	    // resolve view based on action
	    ArrayList<Handler> registeredHandlers = handlers.get(type);
	    
	    Logger.debug("OsoPolar event received (" + type + ") (Handlers: " + registeredHandlers.size() + ")");

	    for( int i = 0; i < registeredHandlers.size(); i++) {
	        Handler eventData = registeredHandlers.get(i);
	        String key = eventData.getPageName();
	        String action = eventData.getPageAction();
	        
	        if(action.equals("display")) {
	        	callIdWithData(key, data);
	        } else {
	        	clearId(key);
	        }
	    }
	}

	
	private void hideUnusedContainers() {
		
		for(String viewName : containers.keySet() ) {
			ViewGroup container = containers.get(viewName);
			if(container.getChildCount() == 0) {
				container.setVisibility(View.INVISIBLE);
			} else {
				container.setVisibility(View.VISIBLE);
			}
		}
	}

	private ViewGroup resolveContainerForContext(ViewableContext context) {
	    ViewGroup container;
	    if(context.getContainerName() != null) {
	        container = containers.get(context.getContainerName());
	    } else {
	        container = rootView;
	    }
	    
	    hideUnusedContainers();
	    
	    return container;
	}
	
	
	private void callIdWithData(String callId, Object data) throws Exception {
		
		Logger.debug("callID: " + callId);
		
		PageContext pcontext = factory.getPageContextWithId(callId);
		
		if(pcontext != null) {
			gotoPageWithIdAndData(callId, data, pcontext);
			return;
		}
		
		BlockContext bcontext = factory.getBlockContextWithId(callId);
		
		if(bcontext != null) {
			displayBlockWithIdAndData(callId, data, bcontext);
			return;
		}
		
		CommandContext ccontext = factory.getCommandContextWithId(callId);
		
		if(ccontext != null) {
			executeCommandWithIdAndData(callId, data, ccontext);
			return;
		}
	}

	private void clearId(String viewId) throws Exception {
		
		Logger.debug("clearId: " + viewId);
		
		PageContext pcontext = factory.getPageContextWithId(viewId);
		
		if(pcontext != null) {
			clearPageWithId(viewId);
			return;
		}
		
		BlockContext bcontext = factory.getBlockContextWithId(viewId);
		
		if(bcontext != null) {
			clearBlockWithId(viewId);
			return;
		}
	}
	

	private void extractHandlers() {
		
	    handlers = new HashMap<String, ArrayList<Handler>>();
	    
	    ArrayList<Map> sections = new ArrayList<Map>();
	    sections.add(pages);
	    sections.add(blocks);
	    sections.add(commands);
	    
	    for ( Map<String,Map<String, ?>> section : sections) {

	        for (String partName : section.keySet()) {
	            
	        	Map<String, ?> part = section.get(partName);
	        	ArrayList<?> tmpevents = (ArrayList<?>) part.get("handlers");
	            
	            String eventTypeName;
	            String eventTypeAction;

	            for(Object event : tmpevents) {

	                if (event instanceof String) {
	                    eventTypeName = (String) event;
	                    eventTypeAction = "display";
	                } else {
	                	Map<String,String> eventObj = (Map<String,String>)event;
	                    eventTypeName = eventObj.get("type");
	                    eventTypeAction = eventObj.get("action");
	                }
	                ArrayList<Handler> events = handlers.get(eventTypeName);

	                if(events == null) {
	                	events = new ArrayList<Handler>();
	                	handlers.put(eventTypeName, events);

	                	NotificationCenter.defaultCenter().addObserver(this, "onReceive", eventTypeName);
	                }
	                Handler handler = new Handler();
	                handler.setPageName(partName);
	                handler.setPageAction(eventTypeAction);
	                events.add(handler);
	                Logger.verbose("Handler added: " + eventTypeName + ", " + partName + ", " +eventTypeAction);
	            }
	        }
	    }
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
