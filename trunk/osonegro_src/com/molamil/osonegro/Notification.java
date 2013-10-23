package com.molamil.osonegro;

import java.util.Map;

public class Notification {
	private String type;
	private Object data;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public static Notification notificationWithName(String name, Object object) {
		Notification note = new Notification();
		note.setType(name);
		note.setData(object);
		return note;
	}
	
	public Object get(String key) {
		if(data instanceof Map) {
			Map map = (Map)data;
			Object obj = map.get(key);
			
			String myvar = null;
			return obj;
		}
		return null;
	}
}
