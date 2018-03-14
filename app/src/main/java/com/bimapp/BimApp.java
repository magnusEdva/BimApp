package com.bimapp;

import android.app.Application;
import android.content.SharedPreferences;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
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

        if(!isLoggedIn()){
            logIn();
        }

    }

    public OAuthHandler getMOAuth(){
        return mOAuth;
    }


    public void storeRefreshToken(String refreshToken){
        SharedPreferences prefs = getSharedPreferences("oAuth", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("RefreshToken", refreshToken);
        edit.apply();
    }

    public String getRefreshToken(){
        SharedPreferences prefs = getSharedPreferences("oAuth", MODE_PRIVATE);
        return prefs.getString("RefreshToken", null);
    }
    public void storeAccesToken(String accessToken, int expiration ){
        SharedPreferences prefs = getSharedPreferences("oAuth", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("AccessToken", accessToken);
        edit.putLong("ExpiresAt",System.currentTimeMillis() + (expiration * 1000));
        edit.apply();
    }

    public String getAcessToken(){
        SharedPreferences prefs = getSharedPreferences("oAuth", MODE_PRIVATE);
        return prefs.getString("AccessToken", null);
    }

    public Boolean isValidAccessToken(){
        SharedPreferences prefs = getSharedPreferences("oAuth", MODE_PRIVATE);
        Long expirationDate =  prefs.getLong("AccessToken", 0L);

        return expirationDate != 0L && expirationDate < System.currentTimeMillis() - 100000;

    }

    public boolean isLoggedIn(){
        if(isValidAccessToken())
            return true;
        else if(getRefreshToken() != null){
         //   return  refreshToken();
        }
        return false;

    }

    public boolean refreshToken(String code, String grantType){
        mOAuth.getAccessToken(code, grantType);
        return false;
    }

    public void logIn(){
        mOAuth.launchBrowser();
    }

    public RequestQueue getRequestQueue(){
        return requestQueue;
    }

    public <T> void add(Request<T> req){
        req.setTag("lol");
        requestQueue.add(req);
    }
    public void cancel(){
        requestQueue.cancelAll("lol");
    }
}
