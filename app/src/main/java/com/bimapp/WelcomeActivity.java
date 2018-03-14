package com.bimapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Håkon on 09.03.2018.
 */

public class WelcomeActivity extends AppCompatActivity {
    BimApp mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mApplication = (BimApp) this.getApplicationContext();
        mApplication.getMOAuth().launchBrowser();

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(getIntent().getData() != null) {
            String code = getIntent().getData().getQueryParameter("code");
            mApplication.refreshToken(code);
        }
    }
}
