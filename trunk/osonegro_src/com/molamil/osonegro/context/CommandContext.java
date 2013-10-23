package com.molamil.osonegro.context;

import java.util.Map;

import com.molamil.osonegro.master.CommandMaster;

public class CommandContext implements Context {

	private String id;
	private String type;
	private Map<String,?> props;
	private CommandMaster master;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Map<String, ?> getProps() {
		return props;
	}
	public void setProps(Map<String, ?> props) {
		this.props = props;
	}
	public CommandMaster getMaster() {
		return master;
	}
	public void setMaster(CommandMaster master) {
		this.master = master;
	}
	
	
}
