package com.molamil.model;

import org.json.JSONException;
import org.json.JSONObject;

public interface PostableObject {

	public JSONObject postableObject() throws JSONException;
	public String postableNS();
	public void objectFromDictionary(JSONObject dictionary);
}