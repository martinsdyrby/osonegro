package com.molamil.osonegro;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class OsoNegroParser {

	public final static String OSONEGRO = "osonegro";
	public final static String PAGE = "page";
	public final static String BLOCK = "block";
	public final static String COMMAND = "command";
	public final static String COMMANDS = "commands";
	public final static String VIEWS = "views";
	public final static String BLOCKS = "blocks";
	public final static String PAGES = "pages";
	public final static String PROP = "prop";
	public final static String PROPS = "props";
	public final static String ITEM = "item";
	public final static String HANDLER = "handler";
	public final static String NAME = "name";
	public final static String TYPE = "type";
	public final static String ACTION = "action";
	public final static String ID = "id";
	public final static String EXTENDS = "extends";
	public final static String DEPENDS = "depends";
	public final static String MANAGER = "manager";
	public final static String HANDLERS = "handlers";
	public final static String TARGET = "target";
	public final static String CONTAINER = "container";
	
	
    private static final String ns = null;
    
    public Map<String, Map<String,Object>> parse(InputStream in) throws XmlPullParserException, IOException {
    	
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(in, null);
        parser.nextTag();
        return readFeed(parser);
    }
    
    
    private Map<String, Map<String,Object>> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
    	Map<String, Map<String, Object>> entries = new HashMap<String, Map<String,Object>>();
    	Map<String,Object> commands = new HashMap<String, Object>();
    	Map<String,Object> props = new HashMap<String, Object>();

    	parser.require(XmlPullParser.START_TAG, ns, OSONEGRO);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals(COMMANDS)) {
            	commands = readCommands(parser);
            } else if (name.equals(VIEWS)) {
            	entries = readViews(parser);
            } else if(name.equals(PROP)) {
            	String propName = parser.getAttributeValue(ns, NAME);
            	props.put(propName, readProp(parser));
            } else {
                skip(parser);
            }
        }  
        parser.require(XmlPullParser.END_TAG, ns, OSONEGRO);

        entries.put(COMMANDS, commands);
        entries.put(PROPS, props);

        return entries;
    }
    
    private Map<String, Map<String, Object>> readViews(XmlPullParser parser) throws XmlPullParserException, IOException {
    	Map<String, Map<String, Object>> entries = new HashMap<String, Map<String,Object>>();
    	Map<String,Object> pages = new HashMap<String, Object>();
    	Map<String,Object> blocks = new HashMap<String, Object>();
    	Map item;
    	parser.require(XmlPullParser.START_TAG, ns, VIEWS);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(PAGE)) {
            	item = readPage(parser);
            	pages.put((String) item.get(ID), item);
            } else if(name.equals(BLOCK)) {
            	item = readBlock(parser);
            	blocks.put((String) item.get(ID), item);
            } else {
                skip(parser);
            }
        }  
        parser.require(XmlPullParser.END_TAG, ns, VIEWS);
        
        entries.put(PAGES, pages);
        entries.put(BLOCKS, blocks);
		return entries;
	}


	private Map<String,Object> readCommands(XmlPullParser parser) throws XmlPullParserException, IOException {
    	Map<String,Object> commands = new HashMap<String, Object>();
    	
    	parser.require(XmlPullParser.START_TAG, ns, COMMANDS);
    	Map item;
    	
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if(name.equals(COMMAND)) {
            	item = readCommand(parser);
            	commands.put((String) item.get(ID), item);
            } else {
                skip(parser);
            }
        } 
    	parser.require(XmlPullParser.END_TAG, ns, COMMANDS);
    	
    	return commands;
    	
    }


	private Map readCommand(XmlPullParser parser) throws XmlPullParserException, IOException {
		
	    parser.require(XmlPullParser.START_TAG, ns, COMMAND);
	    Map<String, Object> command = new HashMap<String, Object>();

	    command.put(ID, parser.getAttributeValue(ns, ID));
	    command.put(TYPE, parser.getAttributeValue(ns, TYPE));
	    Map<String,Object> props = new HashMap<String,Object>(); 
	    command.put(PROPS, props);
	    List<Object> handlers = new ArrayList<Object>();
	    command.put(HANDLERS, handlers);
	    
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            
            String name = parser.getName();
            
            if(name.equals(PROP)) {
            	String propName = parser.getAttributeValue(ns, NAME);
            	props.put(propName, readProp(parser));
            } else if(name.equals(HANDLER)) {
            	handlers.add(readHandler(parser));
            }
        }
        
        parser.require(XmlPullParser.END_TAG, ns, COMMAND);
	    
	    return command;
	}


	private Map<String, Object> readBlock(XmlPullParser parser) throws XmlPullParserException, IOException {
	    parser.require(XmlPullParser.START_TAG, ns, BLOCK);
	    Map<String, Object> view = readView(parser);
	    parser.require(XmlPullParser.END_TAG, ns, BLOCK);
		return view;
	}


	private Map<String, Object> readPage(XmlPullParser parser) throws XmlPullParserException, IOException {
		
	    parser.require(XmlPullParser.START_TAG, ns, PAGE);
	    Map<String, Object> view = readView(parser); 
	    parser.require(XmlPullParser.END_TAG, ns, PAGE);
		return view;
	}
	
	private Map<String, Object> readView(XmlPullParser parser) throws XmlPullParserException, IOException {
		
	    Map<String, Object> view = new HashMap<String, Object>();

	    view.put(ID, parser.getAttributeValue(ns, ID));
	    String depends = parser.getAttributeValue(ns, DEPENDS);
	    String[] aDepends = depends != null ? depends.split(",") : null;
	    view.put(DEPENDS, aDepends);
	    view.put(EXTENDS, parser.getAttributeValue(ns, EXTENDS));
	    view.put(CONTAINER, parser.getAttributeValue(ns, CONTAINER));
	    view.put(MANAGER, parser.getAttributeValue(ns, MANAGER));
	    view.put(TYPE, parser.getAttributeValue(ns, TYPE));
	    view.put(TARGET, parser.getAttributeValue(ns, TARGET));
	    Map<String,Object> props = new HashMap<String,Object>(); 
	    view.put(PROPS, props);
	    List<Object> handlers = new ArrayList<Object>();
	    view.put(HANDLERS, handlers);
	    
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            
            String name = parser.getName();
            
            if(name.equals(PROP)) {
            	String propName = parser.getAttributeValue(ns, NAME);
            	props.put(propName, readProp(parser));
            } else if(name.equals(HANDLER)) {
            	handlers.add(readHandler(parser));
            }
        }
	    
	    return view;
		
	}

	
	private Object readHandler(XmlPullParser parser) throws XmlPullParserException, IOException {
		Object result;
		
	    parser.require(XmlPullParser.START_TAG, ns, HANDLER);
	    
	    String handlerType = parser.getAttributeValue(ns, TYPE);
	    String actionType = parser.getAttributeValue(ns, ACTION);
	    
	    if(actionType == null) {
	    	result = handlerType;
	    } else {
	    	Map<String,String> tmp = new HashMap<String, String>();
	    	tmp.put(TYPE, handlerType);
	    	tmp.put(ACTION, actionType);
	    	
	    	result = tmp;
	    }
	    parser.nextTag();
	    parser.require(XmlPullParser.END_TAG, ns, HANDLER);

	    return result;
	}


	private Object readProp(XmlPullParser parser) throws XmlPullParserException, IOException {
		Object result = null;
		
	    parser.require(XmlPullParser.START_TAG, ns, PROP);
	    
	    String simplevalue = parser.getAttributeValue(ns, "value");
	    
	    if(simplevalue != null) {
	    	result = simplevalue;
	    	parser.nextTag();
	    } else {
	    	Map<String,Object> props = new HashMap<String, Object>();
	    	ArrayList<Object> items = new ArrayList<Object>();
	    	boolean useItems = false;
	    	boolean useProps = false;
	        while (parser.next() != XmlPullParser.END_TAG) {
	        	int et = parser.getEventType();
	            if (et != XmlPullParser.START_TAG) {
	            	result = parser.getText();
	                continue;
	            }
	            
	            String name = parser.getName();
	            
	            if(name.equals(PROP)) {
	            	useProps = true;
	            	String propName = parser.getAttributeValue(ns, NAME);
	            	props.put(propName, readProp(parser));
	            } else if(name.equals(ITEM)) {
	            	useItems = true;
	            	items.add(readItem(parser));
	            }
	        }
	        
	        if(useItems || useProps) {
	        	result = (useProps) ? props : items;
	        }
	    }
	    
	    
	    parser.require(XmlPullParser.END_TAG, ns, PROP);
		return result;
	}
	
	private Object readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
		
		Object result;
		
		parser.require(XmlPullParser.START_TAG, ns, ITEM);
		
    	Map<String,Object> props = new HashMap<String, Object>();
    	ArrayList<Object> items = new ArrayList<Object>();
    	String text = "";
    	boolean useProps = false;
        while (parser.next() != XmlPullParser.END_TAG) {
            String name = parser.getName();
            
            if(parser.getEventType() == XmlPullParser.TEXT) {
            	text = parser.getText();
            } else if(name.equals(PROP)) {
            	useProps = true;
            	String propName = parser.getAttributeValue(ns, NAME);
            	props.put(propName, readProp(parser));
            } else if(name.equals(ITEM)) {
            	items.add(readItem(parser));
            }
        }
        if(useProps) {
        	result = props;
        } else if(items.size() < 0) {
        	result = items;
        } else {
        	result = text;
        }
	    
		parser.require(XmlPullParser.END_TAG, ns, ITEM);
		
		return result;
	}


	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }
	
}
