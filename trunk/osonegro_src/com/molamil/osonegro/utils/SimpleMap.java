package com.molamil.osonegro.utils;

import java.util.HashMap;
import java.util.Map;

public class SimpleMap {
	static public Map<String,String> stringMap(String... args) {
		Map<String,String> map = new HashMap<String, String>();
		for(int i = 0; i < args.length; i+=2) {
			map.put(args[i], args[i+1]);
		}
		return map;
	}
	
	static public Map<String,Object> objectMap(Object... args) {
		Map<String,Object> map = new HashMap<String, Object>();
		for(int i = 0; i < args.length; i+=2) {
			map.put((String) args[i], args[i+1]);
		}
		return map;
	}
}
