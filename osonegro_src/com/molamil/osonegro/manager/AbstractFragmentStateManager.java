package com.molamil.osonegro.manager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;

import com.molamil.osonegro.NotificationCenter;
import com.molamil.osonegro.OsoNegroApp;
import com.molamil.osonegro.OsoNegroIntent;

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
        if(getFragment() != null && getView() == null) {
            Runnable task = new Runnable() {
                public void run() {
                    /* Do something… */
                    changeState();
                }
            };
            worker.schedule(task, 100, TimeUnit.MILLISECONDS);
            return;
        }
        super.changeState();
    }

    protected void showFragment() {
        FragmentManager fragmentManager = OsoNegroApp.getAndroidActivity().getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.show(getFragment());
        transaction.commit();
        fragmentManager.executePendingTransactions();
    }
}
