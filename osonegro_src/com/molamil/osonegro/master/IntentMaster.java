package com.molamil.osonegro.master;



import com.molamil.osonegro.OsoNegroApp;
import com.molamil.osonegro.OsoNegroIntent;
import com.molamil.osonegro.utils.Logger;

/**
 * Created by martinschiothdyrby on 07/05/15.
 */
public class IntentMaster extends AbstractMaster {

    public void doDisplay() {

        String type = getContext().getType();
        Logger.debug("Type: " + type);
        try {
            Class clzz = Class.forName(type);
            OsoNegroIntent intent = new OsoNegroIntent(clzz);
            setTarget(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
