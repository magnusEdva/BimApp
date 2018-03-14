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
        setContentView(R.layout.activity_welcome);
    }

    public void buttonOnClick(View v){
        mApplication = (BimApp) this.getApplicationContext();
        mApplication.getMOAuth().launchBrowser();
        if(getIntent().getData() != null) {
            String code = getIntent().getData().getQueryParameter("code");
            mApplication.refreshToken(code, OAuthHandler.GRANT_TYPE_AUTHORIZATION_CODE);
            Intent intent = new Intent(this, ProjectsViewActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

    }
}
