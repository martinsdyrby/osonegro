package com.molamil.osonegro.master;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;

import com.molamil.osonegro.OsoNegroApp;
import com.molamil.osonegro.manager.AbstractStateManager;
import com.molamil.osonegro.utils.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by martinschiothdyrby on 09/05/15.
 */
public class FragmentMaster extends AbstractMaster {

    private static Map<String,Fragment> instances;
    private Fragment fragment;

    private int enterAnimation = 0;
    private int exitAnimation = 0;

    public void doDisplay() {
        //fragment = getContextInstanceFromId(this.getContext().getId());
        if(fragment == null) {

            String type = getContext().getType();
            try {
                Class clzz = Class.forName(type);
                fragment = (Fragment) clzz.newInstance();
                addContextInstanceForId(fragment, getContext().getId());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        setTarget(fragment);
        FragmentManager fragmentManager = OsoNegroApp.getAndroidActivity().getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(fragment.isAdded()) {
            transaction.remove(fragment);
        }
        transaction.add(getContext().getContainerId(), fragment);
        transaction.hide(fragment);
        transaction.commit();
        fragmentManager.executePendingTransactions();
    }

    public void doClear() {
        FragmentManager fragmentManager = OsoNegroApp.getAndroidActivity().getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
        fragment = null;
    }

    public void doDestroy() {}

    static private Fragment getContextInstanceFromId(String id) {

        if(instances == null) {
            instances = new HashMap<String, Fragment>();
        }

        return instances.get(id);
    }

    static private void addContextInstanceForId(Fragment instance, String id) {

        if(instances == null) {
            instances = new HashMap<String, Fragment>();
        }

        instances.put(id, instance);
    }

    public void setEnterAnimation(int animation) {
        enterAnimation = animation;
    }

    public void setExitAnimation(int animation) {
        exitAnimation = animation;
    }
}
