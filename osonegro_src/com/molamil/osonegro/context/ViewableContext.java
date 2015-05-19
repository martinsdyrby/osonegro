package com.molamil.osonegro.context;

import android.view.ViewGroup;

import com.molamil.osonegro.manager.StateManager;


public interface ViewableContext extends Context {

	public String getXib();
	public void setXib(String xib);
	public String[] getDepends();
	public void setDepends(String[] depends);
	public String getContainerName();
	public void setContainerName(String containerName);	
	public ViewGroup getContainer();
	public void setContainer(ViewGroup container);
	public int getContainerId();
	public void setContainerId(int id);
	public StateManager getManager();
	public void setManager(StateManager manager);	
	
}
