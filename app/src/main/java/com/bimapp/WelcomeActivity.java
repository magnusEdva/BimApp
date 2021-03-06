package com.bimapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.bimapp.model.data_access.network.Callback;
import com.bimapp.model.data_access.network.oauth.OAuthHandler;

/**
 * Activity that manages the startup and login of the application.
 */

public class WelcomeActivity extends AppCompatActivity {
    BimApp mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_login);
        mApplication = (BimApp) this.getApplicationContext();

        if(mApplication.checkLogIn()) {
            openProjectsView();

        }
        Log.d("create", "create");

    }

    public void buttonOnClick(View v){
        mApplication.getMOAuth().launchBrowser();
        finish();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(getIntent().getData() != null && getIntent().getData().getQueryParameter(OAuthHandler.OAUTH_REQUEST_CODE) != null) {
            String code = getIntent().getData().getQueryParameter(OAuthHandler.OAUTH_REQUEST_CODE);
            mApplication.refreshToken(code, OAuthHandler.GRANT_TYPE_AUTHORIZATION_CODE, new networkCallback());
        }
    }

    public void openProjectsView(){
        Intent intent = new Intent(this, ProjectsViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    /**
     * Network callback for when a user logs in with a browser.
     */
    private class networkCallback implements Callback<String>{

        @Override
        public void onError(String response) {}

        @Override
        public void onSuccess(String response) {
            openProjectsView();
        }

    }
}
