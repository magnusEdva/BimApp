package com.bimapp;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bimapp.model.entity.Project;
import com.bimapp.model.network.Callback;
import com.bimapp.model.network.oauth.OAuthHandler;

/**
 * Context class
 */

public class BimApp extends Application{
    /**
     * This is the reference to the selected project. It is stored in
     * a SharedPreference upon selection, and used with the API for
     * acquiring the expected results.
     */
    private Project mActiveProject;
    /**
     * This object is responsible for all Token handling.
     * Across the entire application.
     */
    private OAuthHandler mOAuth;
    /**
     * This is the network queue.
     */
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
        req.setTag(tag);
        requestQueue.add(req);
    }
    public void cancelRequestQueue(String tag){
        requestQueue.cancelAll(tag);
    }

    public void setActiveProject(Project project){

    }
}
