package com.bimapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bimapp.model.network.Callback;
import com.bimapp.model.oauth.OAuthHandler;

import org.json.JSONArray;
import org.json.JSONObject;

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

        if(mApplication.checkLogIn())
            openProjectsView();


    }

    public void buttonOnClick(View v){

        mApplication.getMOAuth().launchBrowser();

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(getIntent().getData() != null && getIntent().getData().getQueryParameter("code") != null) {
            String code = getIntent().getData().getQueryParameter("code");
            mApplication.refreshToken(code, OAuthHandler.GRANT_TYPE_AUTHORIZATION_CODE, new networkCallback());
        }
    }

    public void openProjectsView(){
        Intent intent = new Intent(this, ProjectsViewActivity.class);
        startActivity(intent);
    }

    private class networkCallback implements Callback{

        @Override
        public void onError(String response) {}

        @Override
        public void onSuccess(JSONArray arr) {
            openProjectsView();
        }

        @Override
        public void onSuccess(JSONObject obj) {
            openProjectsView();
        }
    }
}
