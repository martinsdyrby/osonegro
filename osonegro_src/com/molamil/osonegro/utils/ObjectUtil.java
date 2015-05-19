package com.molamil.osonegro.utils;


import android.os.Bundle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.molamil.model.PostableObject;
import com.molamil.osonegro.OsoNegroIntent;

public class ObjectUtil {
	static private Map<String,Object> propsList;
	static private Set<String> propsKeySet;

	public static void mergePropsWithObject(Object props, Object target) {
		if(props == null) return;
		if(target == null) return;
		
		if(props instanceof JSONObject) {
			JSONObject obj = (JSONObject) props;
			mergePropsWithObject(obj, target);
			return;
		}
		
		if(props instanceof Map) {
			Map obj = (Map) props;
			mergePropsWithObject(obj, target);
			return;
		}
	}
	public static void mergePropsWithObject(JSONObject props, Object target) {
		if(props == null) return;
		if(target == null) return;
		
		Iterator it = props.keys();
		while(it.hasNext()) {
			String entryKey = (String) it.next();
			try {
				setValueForKey(target, entryKey, props.opt(entryKey));
			} catch (IllegalAccessException e) {
				Logger.error("ObjectUtil.mergePropsWithObject", e);
			} catch (InvocationTargetException e) {
				Logger.error("ObjectUtil.mergePropsWithObject", e);
			}
		}
	}
	public static void mergePropsWithObject(Map<String,?> props, Object target) {
		if(props == null) return;
		if(target == null) return;
		
		for (String entryKey : props.keySet()) {
			try {
				setValueForKey(target, entryKey, props.get(entryKey));
			} catch (IllegalAccessException e) {
				Logger.error("ObjectUtil.mergePropsWithObject", e);
			} catch (InvocationTargetException e) {
				Logger.error("ObjectUtil.mergePropsWithObject", e);
			}
		}
	}

	public static void mergePropsWithObject(Bundle bundle, Object target) {
		if(bundle == null) return;

		Set<String> keys = bundle.keySet();
		for (String key : keys) {
			Object o = bundle.get(key);
			try {
				setValueForKey(target, key, o);
			} catch (IllegalAccessException e) {
				Logger.error("ObjectUtil.mergePropsWithObject", e);
			} catch (InvocationTargetException e) {
				Logger.error("ObjectUtil.mergePropsWithObject", e);
			}

		}

	}

	public static String fieldToSetter(String name)
	{
	    return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
	public static String fieldToGetter(String name)
	{
	    return "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
	}
	
	public static Object getValueForKey(Object anObject, String key) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(anObject instanceof JSONObject) return ((JSONObject) anObject).opt(key);
		if(anObject instanceof Map) return ((Map) anObject).get(key);
		String getter = fieldToGetter(key);
		return anObject.getClass().getMethod(getter).invoke(anObject);
	}
	
	
	public static boolean setValueForKey(Object anObject, String key, Object value) throws IllegalAccessException, InvocationTargetException {
		Class<?> cls;
		boolean isIntent = anObject instanceof OsoNegroIntent;
		OsoNegroIntent intent = null;
		if(isIntent) {
			intent = (OsoNegroIntent) anObject;
			cls = intent.getTargetClass();
		} else {
			cls = anObject.getClass();
		}
		String setter = fieldToSetter(key);
		value = resolveExpressions(value);
		for (Method method : cls.getMethods()) {
			if (setter.equals(method.getName())) {
				if(isIntent && intent != null) {
					intent.putExtra(key, value);
					return true;
				} else {
					try {
						method.invoke(anObject, value);
					} catch (IllegalArgumentException e) {
						Logger.error(setter + " = " + value, e);
					}
					return true;
				}
			}
		}
		return false;
	}

	private static Object resolveExpressions(Object value) {
		String sValue;
		if(value != null && value instanceof String) {
			sValue = (String) value;

			if(sValue.indexOf("${") > -1) {
				
				String propValue;
				for(String key : propsKeySet) {
					propValue = (String) propsList.get(key);
					sValue = sValue.replace("${"+key+"}", propValue);
				}
			}
			
			
			if(sValue.equals("true")) {
				value = true;
			} else if(sValue.equals("false")) {
				value = false;
			} else if(isNumeric(sValue)) {
				value = Double.parseDouble(sValue);
			} else {
				value = sValue;
			}
		}
		
		return value;
	}

	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	public static Map<String, Object> getPropsList() {
		return propsList;
	}

	public static void setPropsList(Map<String, Object> propsList) {
		ObjectUtil.propsList = propsList;
	}

	public static Set<String> getPropsKeySet() {
		return propsKeySet;
	}

	public static void setPropsKeySet(Set<String> propsKeySet) {
		ObjectUtil.propsKeySet = propsKeySet;
	}

	public static boolean executeMethod(Object target, String methodName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		target.getClass().getMethod(methodName).invoke(target);
		return false;
	}
	
	public static JSONObject postableDataWithObjects(PostableObject... object) {

		JSONObject obj = new JSONObject();
		JSONObject data = new JSONObject();
		try {
			obj.put("data", data);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i = 0; i < object.length; i++) {
			PostableObject pObj = object[i];
			try {
				data.put(pObj.postableNS(), pObj.postableObject());
			} catch (JSONException e) {
				Logger.error("ObjectUtil.postableDataWithObjects", e);
			}
		}
		return obj;
	}
}
