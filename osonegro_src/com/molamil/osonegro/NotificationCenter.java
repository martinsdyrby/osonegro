package com.molamil.osonegro;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.util.Log;

import com.molamil.model.NavigationItem;
import com.molamil.model.NavigationItemData;
import com.molamil.osonegro.utils.Logger;

public class NotificationCenter {

	private Map<String,ArrayList<NotificationHandler>> _observers;
	private static NotificationCenter _defaultCenter;
	public static NotificationCenter defaultCenter() {
		if(_defaultCenter == null) {
			_defaultCenter = new NotificationCenter();
		}
		return _defaultCenter;
	}

	public static void destroy() {
		_defaultCenter = null;
	}
	public NotificationCenter() {
		_observers = new HashMap<String, ArrayList<NotificationHandler>>();
	}
	public void addObserver(Object target, String methodName, String type) {
		
		ArrayList<NotificationHandler> obs = _observers.get(type); 
		if(obs == null) {
			_observers.put(type, new ArrayList<NotificationHandler>());
			obs = _observers.get(type);
		}
		obs.add(new NotificationHandler(target, methodName));
	}
	
	public void removeObserver(Object target, String type) {
		ArrayList<NotificationHandler> obs = _observers.get(type); 
		if(obs == null) {
			return;
		}
		for(int i = 0; i < obs.size(); i++) {
			NotificationHandler h = obs.get(i);
			if(h.getTarget().equals(target)) {
				obs.remove(i);
				break;
			}
		}
		obs.remove(target);
	} 

	public Notification addPageBackAction(String aName, String section) {
	    NavigationItem navItem = NavigationItem.navigationItem(aName, null);
	    NavigationItemData data = NavigationItemData.navigationItemData(navItem, section);
	    postNotification("addBackAction", data);
	    return Notification.notificationWithName(aName, null);
	}
	
	public Notification addPageBackAction(String aName, String section, JSONObject anObject) {
	    NavigationItem navItem = NavigationItem.navigationItem(aName, anObject);
	    NavigationItemData data = NavigationItemData.navigationItemData(navItem, section);
	    postNotification("addBackAction", data);
	    return Notification.notificationWithName(aName, anObject);
	}

	public void postNotification(String type) {
		postNotification(type, null);
	}
	
	public void postNotification(String type, Object data) {
		Notification note = new Notification();
		note.setType(type);
		note.setData(data);
		postNotification(note);
	}
	
	public void postNotification(Notification note) {
		// Log.d("RE/ ","NotificationCenter Notification: " + note);
		String type = note.getType();
		Log.d("NOTIFICATION",":: postNotification: " + type);
		ArrayList<NotificationHandler> obs = _observers.get(type);
		Log.d("NOTIFICATION", ":: num handlers: " + (obs != null ? obs.size() : "" + 0));
		if(obs != null) {
			for(NotificationHandler handler : obs) {
				String methodName = handler.getMethodName();
				Object target = handler.getTarget();
				Log.d("NOTIFICATION", ":: Type: " + type + " Method name: " + methodName);
				Class[] paramType;
				paramType = new Class[1];
				paramType[0] = Notification.class;
				Method method;
				try {
					method = target.getClass().getMethod(methodName, paramType);
					method.invoke(target, note);
				} catch (Exception e) {
					Logger.error("NotificationCenter.postNotification", e);
				}
			}
		}
	}

	public Notification addPageBackAction(String aName, String section,
			Map<String, ?> anObject) {
	    NavigationItem navItem = NavigationItem.navigationItem(aName, anObject);
	    NavigationItemData data = NavigationItemData.navigationItemData(navItem, section);
	    postNotification("addBackAction", data);
	    return Notification.notificationWithName(aName, anObject);
	}
}

class NotificationHandler {
	public NotificationHandler(Object target, String methodName) {
		setTarget(target);
		setMethodName(methodName);
	}
	private Object target;
	private String methodName;
	public Object getTarget() {
		return target;
	}
	public void setTarget(Object target) {
		this.target = target;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}	
}