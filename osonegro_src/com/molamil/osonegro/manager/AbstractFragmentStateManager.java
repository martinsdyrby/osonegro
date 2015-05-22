package com.molamil.osonegro.manager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Handler;
import android.view.View;

import com.molamil.osonegro.NotificationCenter;
import com.molamil.osonegro.OsoNegroApp;
import com.molamil.osonegro.OsoNegroIntent;
import com.molamil.osonegro.utils.Logger;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by martinschiothdyrby on 09/05/15.
 */
public class AbstractFragmentStateManager extends AbstractStateManager {
    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();

    public View getView() {
        if(target instanceof Fragment) {
            return (View)((Fragment) target).getView();
        }
        return (View)target;
    }

    public Fragment getFragment() {
        return (Fragment) target;
    }

    protected void changeState() {
        Logger.debug("AbstractFragmentStateManager.changeState: " + getState());
        if(getFragment() != null && getView() == null) {
            Logger.debug("AbstractFragmentStateManager.changeState wait for fragment: " + getState());
            waitForFragment();
            Logger.debug("AbstractFragmentStateManager.changeState should return: " + getState());
            return;
        } else {
            Logger.debug("AbstractFragmentStateManager.changeState will not return: " + getState());
            super.changeState();
        }
    }

    protected void waitForFragment() {
        if(getFragment() != null && getView() == null) {
            Runnable task = new Runnable() {
                public void run() {
                    /* Do something… */
                    waitForFragment();
                }
            };
            worker.schedule(task, 100, TimeUnit.MILLISECONDS);
            return;
        }
        Handler mainHandler = new Handler(OsoNegroApp.getAndroidActivity().getMainLooper());
        Runnable mainTask = new Runnable() {
            public void run() {
                changeState();
            }
        };
        mainHandler.post(mainTask);
    }

    protected void showFragment() {
        FragmentManager fragmentManager = OsoNegroApp.getAndroidActivity().getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.show(getFragment());
        transaction.commit();
        fragmentManager.executePendingTransactions();
    }
}
