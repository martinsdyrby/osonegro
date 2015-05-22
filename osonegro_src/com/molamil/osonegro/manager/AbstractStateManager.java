package com.molamil.osonegro.manager;

import com.molamil.osonegro.NotificationCenter;
import com.molamil.osonegro.OsoNegroApp;
import com.molamil.osonegro.OsoNegroIntent;
import com.molamil.osonegro.utils.Logger;

public class AbstractStateManager implements StateManager {

	public final static String STATE_IN = "STATE_IN";
	public final static String STATE_ON = "STATE_ON";
	public final static String STATE_OUT = "STATE_OUT";
	public final static String STATE_OFF = "STATE_OFF";
	public final static String PREV_STATE_OUT = "PREV_STATE_OUT";
	public final static String PREV_STATE_OFF = "PREV_STATE_OFF";
	protected Object target;
	protected String state;

	public Object getTarget() {
		return target;
	}
	public void setTarget(Object target) {
		this.target = target;
	}
	public String getState() {
		return state;
	}
	public void setState(String aState) {
		aState = aState.toUpperCase();
		Logger.verbose("Set state: " + aState);
		if(
			!aState.equals(STATE_IN) && 
			!aState.equals(STATE_ON) && 
			!aState.equals(STATE_OUT) && 
			!aState.equals(STATE_OFF) && 
			!aState.equals(PREV_STATE_OUT) && 
			!aState.equals(PREV_STATE_OFF)
				) {
			Logger.error("Invalid state: "+state);
			return;
		}
		

	    if (!aState.equals(this.state)) {
	    	this.state = aState;
	    	changeState();
	    }
	}
	
	
	protected void changeState() {
		Logger.debug("AbstractStateManager.changeState: " + getState());
		if(target == null) return;

		State newState = new State();
		newState.setState(state);
		newState.setTarget(target);
		
		NotificationCenter.defaultCenter().postNotification(OsoNegroApp.STATE_CHANGE, newState);
		newState = null;
		
		if(state.equals(STATE_IN)) {
			doIn();
		} else if(state.equals(STATE_ON)) {
			doOn();
		} else if(state.equals(STATE_OUT)) {
			doOut();
		} else if(state.equals(STATE_OFF)) {
			doOff();
		} else if(state.equals(PREV_STATE_OUT)) {
			doPreviousOut();
		}
	}
	
	
	
	
	public void doOn() {}
	public void doOff() {}
	public void doOut() {}
	public void doIn() {}
	public void doPreviousOut(){}

}


