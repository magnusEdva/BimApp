package com.bimapp;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import com.bimapp.model.oauth.OAuthHandler;

/**
 * Context class
 */

public class BimApp extends Application {

    private String AuthorizationCode;
    private OAuthHandler mOAuth;

    @Override
    public void onCreate(){
        super.onCreate();
        AuthorizationCode = getAuthorizationCodeFromStorage();
        mOAuth = new OAuthHandler(this);

        if(!isLoggedIn()){
            logIn();
        }

    }

    public OAuthHandler getmOAuth(){
        return mOAuth;
    }

    public String getAuthorizatonCode(){
        return AuthorizationCode;
    }

    public void setAuthorizationCode(String code){
        AuthorizationCode = code;
        SharedPreferences prefs = getSharedPreferences("oAuth", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("AuthorizationCode", AuthorizationCode);
        edit.apply();
    }

    public String getAuthorizationCodeFromStorage(){
        SharedPreferences prefs = getSharedPreferences("oAuth", MODE_PRIVATE);
        return prefs.getString("AuthorizationCode", null);
    }

    public boolean isLoggedIn(){
        return AuthorizationCode != null;
    }

    public void logIn(){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }


}
