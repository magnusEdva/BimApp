package com.bimapp;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Context class
 */

public class BimApp extends Application {

    private String AuthorizationCode;

    @Override
    public void onCreate(){
        super.onCreate();
        AuthorizationCode = getAuthorizationCodeFromStorage();
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
}
