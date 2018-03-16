package com.bimapp;

import android.app.Application;
import android.content.Intent;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bimapp.model.network.Callback;
import com.bimapp.model.oauth.OAuthHandler;

/**
 * Context class
 */

public class BimApp extends Application{


    private OAuthHandler mOAuth;
    private RequestQueue requestQueue;

    @Override
    public void onCreate(){
        super.onCreate();
        requestQueue = Volley.newRequestQueue(this);
        mOAuth = new OAuthHandler(this);
    }

    public OAuthHandler getMOAuth(){
        return mOAuth;
    }


    public void logOut(){
        mOAuth.deleteTokens();
    }

    public String getAcessToken(){
        return mOAuth.getAccessToken();
    }

    public void refreshToken(String code, String grantType, Callback callback){
        mOAuth.getAccessToken(code, grantType, callback);
    }
    public void refreshToken(String code, String grantType){
        mOAuth.getAccessToken(code, grantType);
    }

    public boolean checkLogIn(){
        return mOAuth.isLoggedIn();
    }

    public RequestQueue getRequestQueue(){
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag){
        req.setTag("lol");
        requestQueue.add(req);
    }
    public void cancelRequestQueue(){
        requestQueue.cancelAll("lol");
    }
}
