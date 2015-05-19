package com.molamil.osonegro.manager;

import com.molamil.osonegro.NotificationCenter;
import com.molamil.osonegro.OsoNegroApp;
import com.molamil.osonegro.OsoNegroIntent;

/**
 * Created by martinschiothdyrby on 09/05/15.
 */
public class AbstractActivityStateManager extends AbstractStateManager {
    protected void changeState() {
        if(target == null) return;
        OsoNegroIntent intent = (OsoNegroIntent) target;

        State newState = new State();
        newState.setState(state);
        newState.setTarget(target);

        NotificationCenter.defaultCenter().postNotification(OsoNegroApp.STATE_CHANGE, newState);
        newState = null;

        if(state.equals(STATE_IN)) {
            intent.setEnterAnimation(getEnterAnimation());
            intent.setExitAnimation(getExitAnimation());
        } else if(state.equals(STATE_ON)) {
        } else if(state.equals(STATE_OUT)) {
            setState(STATE_OFF);
        } else if(state.equals(STATE_OFF)) {
        } else if(state.equals(PREV_STATE_OUT)) {
        }
    }

    public int getEnterAnimation(){
        return 0;
    }
    public int getExitAnimation(){
        return 0;
    }
}
