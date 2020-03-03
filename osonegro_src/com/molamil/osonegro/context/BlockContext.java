package com.molamil.osonegro.context;

import java.util.Map;

import android.view.ViewGroup;

import com.molamil.osonegro.manager.StateManager;
import com.molamil.osonegro.master.ViewMaster;

public class BlockContext implements ViewableContext {

	//
	//
	private String id;
	private String type;
	private Map<String,?> props;
	private String xib;
	private String[] depends;
	private String containerName;
	private ViewGroup container;
	private int containerId;
	private StateManager manager;
	private ViewMaster master;
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
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
	public String getXib() {
		return xib;
	}
	public void setXib(String xib) {
		this.xib = xib;
	}
	public String[] getDepends() {
		return depends;
	}
	public void setDepends(String[] depends) {
		this.depends = depends;
	}
	public String getContainerName() {
		return containerName;
	}
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	public ViewGroup getContainer() {
		return container;
	}
	public void setContainer(ViewGroup container) {
		this.container = container;
	}
	public int getContainerId() { return containerId; }
	public void setContainerId(int id) { containerId = id; }
	public StateManager getManager() {
		return manager;
	}
	public void setManager(StateManager manager) {
		this.manager = manager;
	}
	public ViewMaster getMaster() {
		return master;
	}
	public void setMaster(ViewMaster master) {
		this.master = master;
	}
	//

}
