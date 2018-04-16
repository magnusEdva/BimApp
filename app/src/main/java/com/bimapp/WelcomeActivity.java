package com.bimapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bimapp.model.network.Callback;
import com.bimapp.model.network.oauth.OAuthHandler;

/**
 * Created by Håkon on 09.03.2018.
 */

public class WelcomeActivity extends AppCompatActivity {
    BimApp mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_login);
        mApplication = (BimApp) this.getApplicationContext();

        if(mApplication.checkTokensAtStartup()) {
            openProjectsView();

        }

    }

    public void buttonOnClick(View v){

        mApplication.getMOAuth().launchBrowser();
        finish();

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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private class networkCallback implements Callback{

        @Override
        public void onError(String response) {}

        @Override
        public void onSuccess(String response) {
            openProjectsView();
        }

    }
}
