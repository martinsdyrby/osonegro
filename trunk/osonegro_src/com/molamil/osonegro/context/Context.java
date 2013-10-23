package com.molamil.osonegro.context;

import java.util.Map;

public interface Context {

	public String getId();
	public void setId(String id);
	public String getType();
	public void setType(String type);
	public Map<String,?> getProps();
	public void setProps(Map<String,?> props);
	
}
