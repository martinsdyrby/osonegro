package com.molamil.osonegro.manager;

public class SimpleStateManager extends AbstractStateManager {

	public void doOut() {
		setState(STATE_OFF);
	}
	
	public void doIn() {
		setState(STATE_ON);
	}
}
