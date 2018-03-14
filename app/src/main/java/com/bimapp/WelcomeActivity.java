package com.bimapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bimapp.model.oauth.OAuthHandler;

/**
 * Created by Håkon on 09.03.2018.
 */

public class WelcomeActivity extends AppCompatActivity {
    BimApp mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (BimApp) this.getApplicationContext();

        setContentView(R.layout.activity_welcome);

    }

    public void buttonOnClick(View v){

        mApplication.getMOAuth().launchBrowser();

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(getIntent().getData() != null && getIntent().getData().getQueryParameter("code") != null) {
            String code = getIntent().getData().getQueryParameter("code");
            mApplication.refreshToken(code, OAuthHandler.GRANT_TYPE_AUTHORIZATION_CODE);
            Intent intent = new Intent(this, ProjectsViewActivity.class);
            startActivity(intent);
        }
    }
}
