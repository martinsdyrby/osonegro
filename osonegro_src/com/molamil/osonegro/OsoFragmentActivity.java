package com.molamil.osonegro;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by martinschiothdyrby on 08/05/15.
 */
public class OsoFragmentActivity extends FragmentActivity implements OsoActivity {

    private OsoNegroApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            app = OsoNegroApp.getInstance(this);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
    @Override
    public OsoNegroApp getApp() {
        return app;
    }
}
