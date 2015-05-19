package com.molamil.osonegro;

import android.app.Activity;
import android.content.Intent;

import com.molamil.osonegro.utils.Logger;

/**
 * Created by martinschiothdyrby on 07/05/15.
 */
public class OsoNegroIntent extends Intent {

    private Class<?> _cls;
    private int _enterAnimation = 0;
    private int _exitAnimation = 0;
    public OsoNegroIntent(Class<?> cls) {
        super(OsoNegroApp.getAndroidActivity(), cls);
        _cls = cls;
    }
    public void setup(){
        Logger.debug("Intent setup");
        Activity activity = OsoNegroApp.getAndroidActivity();
        activity.startActivity(this);
        activity.overridePendingTransition(_enterAnimation, _exitAnimation);
    }

    public void destroy() {}

    public Intent putExtra(String name, Object value) {
        if (value instanceof String) {
            return putExtra(name, (String) value);
        }
        // TODO. Add more types
        return this;
    }

    public Class<?> getTargetClass() {
        return _cls;
    }


    public void setEnterAnimation(int animation) {
        _enterAnimation = animation;
    }

    public void setExitAnimation(int animation) {
        _exitAnimation = animation;
    }


}
