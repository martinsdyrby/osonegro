package com.molamil.osonegro;

import android.accounts.AccountAuthenticatorActivity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by martinschiothdyrby on 08/05/15.
 */
public class OsoAccountAuthenticatorActivity extends AccountAuthenticatorActivity implements OsoActivity {
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
