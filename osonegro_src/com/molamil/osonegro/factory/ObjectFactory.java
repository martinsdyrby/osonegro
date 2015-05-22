package com.molamil.osonegro.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.molamil.osonegro.context.BlockContext;
import com.molamil.osonegro.context.CommandContext;
import com.molamil.osonegro.context.PageContext;
import com.molamil.osonegro.context.ViewableContext;
import com.molamil.osonegro.manager.StateManager;
import com.molamil.osonegro.master.CommandMaster;
import com.molamil.osonegro.master.FragmentMaster;
import com.molamil.osonegro.master.IntentMaster;
import com.molamil.osonegro.master.ViewMaster;
import com.molamil.osonegro.utils.ObjectUtil;

public class ObjectFactory {

	private final String FRAGMENT_MANAGER = "com.molamil.osonegro.manager.AbstractFragmentStateManager";
	private final String VIEW_MANAGER = "com.molamil.osonegro.manager.AbstractStateManager";
	private final String ACTIVITY_MANAGER = "com.molamil.osonegro.manager.AbstractActivityStateManager";

	private Map<String,PageContext> pageContexts;
	private Map<String,BlockContext> blockContexts;
	private Map<String,CommandContext> commandContexts;
	private Map<String,Object> pagesList;
	private Map<String,Object> blocksList;
	private Map<String,Object> commandsList;
	private Map<String,Object> propsList;
	private Set<String> propsKeySet;

	public void initWithObjects(Map<String,Object> pages, Map<String,Object> blocks, Map<String,Object> commands, Map<String,Object> props) {
		
		pagesList = pages;
		blocksList = blocks;
		commandsList = commands;
		propsList = props;
		propsKeySet = propsList.keySet();

		ObjectUtil.setPropsList(propsList);
		ObjectUtil.setPropsKeySet(propsKeySet);
		
		pageContexts = new HashMap<String, PageContext>();
		blockContexts = new HashMap<String, BlockContext>();
		commandContexts = new HashMap<String, CommandContext>();
	}
	

	public PageContext getPageContextWithId(String pageId) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		PageContext context = pageContexts.get(pageId);
		
		if(context == null) {
			context = new PageContext();
			
			Map<String,Object> page = (Map<String, Object>) pagesList.get(pageId);
			
			if(page == null) {
				return null;
			}
			
			if(page.get("extends") != null) {
				Map<String, Object> parentPage = (Map<String, Object>) pagesList.get(page.get("extends"));
				setViewContextPropertiesOnContext(parentPage, context);
				
				context.setMaster(this.<ViewMaster>setContextValueWithDefault(resolveMaster((String) parentPage.get("type")), context.getMaster()));
				StateManager defaultManager = (StateManager) (context.getMaster() instanceof FragmentMaster ? Class.forName(FRAGMENT_MANAGER).newInstance() : context.getMaster() instanceof IntentMaster ? Class.forName(ACTIVITY_MANAGER).newInstance(): Class.forName(VIEW_MANAGER).newInstance());
				context.setManager(this.<StateManager>setContextValueWithDefault(resolveManagerWithDefault((String) parentPage.get("manager"), defaultManager), context.getManager()));
			}
			
			
			context.setId(pageId);
			setViewContextPropertiesOnContext(page, context);
			context.setMaster(this.<ViewMaster>setContextValueWithDefault(resolveMaster((String) page.get("type")), context.getMaster()));
			context.setManager(this.<StateManager>setContextValueWithDefault(resolveManagerWithDefault((String) page.get("manager"), context.getManager()), context.getManager()));
			context.getMaster().setContext(context);
			
			pageContexts.put(pageId, context);
		}
		
		return context;
	}
	
	public CommandContext getCommandContextWithId(String commandId) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		CommandContext context = commandContexts.get(commandId);

		if(context == null) {
	        context = new CommandContext();
	        
	        Map<String,Object> command = (Map<String, Object>) commandsList.get(commandId);
	        if(command == null) {
	            return null;
	        }
	        context.setId(commandId);
			context.setType(this.<String>setContextValueWithDefault(command.get("type"), null));
	        context.setProps((Map<String, ?>) command.get("props"));
	        context.setMaster(this.<CommandMaster>setContextValueWithDefault(resolveCommandMaster((String) command.get("master")), null));
	        
	        context.getMaster().setContext(context);

	        commandContexts.put(commandId, context);
	    }
	    
	    return context;
	}
	
	public BlockContext getBlockContextWithId(String blockId) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		BlockContext context = blockContexts.get(blockId);
		
		if(context == null) {
			context = new BlockContext();
			
			Map<String,Object> block = (Map<String, Object>) blocksList.get(blockId);
			
			if(block == null) {
				return null;
			}
			
			
			if(block.get("extends") != null) {
				Map<String, Object> parentBlock = (Map<String, Object>) blocksList.get(block.get("extends"));
				
				setViewContextPropertiesOnContext(parentBlock, context);
				
				context.setMaster(this.<ViewMaster>setContextValueWithDefault(resolveMaster((String) parentBlock.get("type")), context.getMaster()));
				StateManager defaultManager = (StateManager) (context.getMaster() instanceof FragmentMaster ? Class.forName(FRAGMENT_MANAGER).newInstance() : context.getMaster() instanceof IntentMaster ? Class.forName(ACTIVITY_MANAGER).newInstance(): Class.forName(VIEW_MANAGER).newInstance());
				context.setManager(this.<StateManager>setContextValueWithDefault(resolveManagerWithDefault((String) parentBlock.get("manager"), defaultManager), context.getManager()));
			}
			
			
			context.setId(blockId);
			setViewContextPropertiesOnContext(block, context);
			context.setMaster(this.<ViewMaster>setContextValueWithDefault(resolveMaster((String) block.get("type")), context.getMaster()));
			context.setManager(this.<StateManager>setContextValueWithDefault(resolveManagerWithDefault((String) block.get("manager"), context.getManager()), context.getManager()));
			context.getMaster().setContext(context);
			
			blockContexts.put(blockId, context);
		}
		
		return context;
	}	

	
	private void setViewContextPropertiesOnContext(Map<String,?> props, ViewableContext context) {
		context.setType(this.<String>setContextValueWithDefault(props.get("target"), context.getType()));
		context.setXib(this.<String>setContextValueWithDefault(props.get("xib"), context.getXib()));
		context.setDepends(this.<String[]>setContextValueWithDefault(props.get("depends"), context.getDepends()));
		context.setProps(this.<Map<String, Map<String,?>>>setContextValueWithDefault(props.get("props"), context.getProps()));
		context.setContainerName(this.<String>setContextValueWithDefault(props.get("container"), context.getContainerName()));
	}
	


	private <T> T setContextValueWithDefault(Object value, Object defaultValue) {
		String sValue = null;
		if(value != null && value instanceof String) {
			sValue = (String) value;
			if(sValue.indexOf("${") > -1) {
				
				String propValue;
				for(String key : propsKeySet) {
					propValue = (String) propsList.get(key);
					sValue = sValue.replace("${"+key+"}", propValue);
				}
			}
			value = sValue;
		}

	    if (value == null) return (T)defaultValue;
	    return (T)value;
	}
	
	private StateManager resolveManagerWithDefault(String managerName, StateManager defaultManager) throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		if(managerName == null) {
	        if(defaultManager == null) {
	            managerName = "com.molamil.osonegro.manager.AbstractStateManager";
	        } else {
	            return defaultManager;
	        }
	    }

		
		if(managerName.indexOf("${") > -1) {
			
			String propValue;
			for(String key : propsKeySet) {
				propValue = (String) propsList.get(key);
				managerName = managerName.replace("${"+key+"}", propValue);
			}
		}
		
	    if(managerName.equals("rightslide")) {
	        managerName = "com.molamil.osonegro.manager.SlideFromRightStateManager";
	    } else if(managerName.equals("leftslide")) {
	        managerName = "com.molamil.osonegro.manager.SlideFromLeftStateManager";
	    }

		return (StateManager) Class.forName(managerName).newInstance();
	}
	
	private ViewMaster resolveMaster(String masterName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if(masterName == null) return null;
	    if(masterName.toLowerCase().equals("definition")) {
	        masterName = "com.molamil.osonegro.master.Definition";
	    } else if(masterName.toLowerCase().equals("layout")) {
	    	masterName = "com.molamil.osonegro.master.Layout";
	    } else if(masterName.toLowerCase().equals("intent")) {
			masterName = "com.molamil.osonegro.master.IntentMaster";
		} else if(masterName.toLowerCase().equals("fragment")) {
			masterName = "com.molamil.osonegro.master.FragmentMaster";
		}
	    ViewMaster master = null;

    	Class masterClass = Class.forName(masterName);
    	Object instance = masterClass.newInstance();
		master =  (ViewMaster) instance;
	    
	    return master;
	}
	
	private CommandMaster resolveCommandMaster(String masterName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
	    if(masterName == null) {
	        masterName = "com.molamil.osonegro.master.Execute";
	    }
		return (CommandMaster) Class.forName(masterName).newInstance();
	}
}
