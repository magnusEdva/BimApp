package com.bimapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bimapp.model.entity.Project;
import com.bimapp.model.network.Callback;
import com.bimapp.model.network.oauth.OAuthHandler;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Application class. Acquired with getApplicationContext. Casted to
 * BimApp.
 */

public class BimApp extends Application {
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
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(this);
        mOAuth = new OAuthHandler(this);
    }

    public OAuthHandler getMOAuth() {
        return mOAuth;
    }

    /**
     * deletes all tokens and cache from storage. After calling this, the presenter should change to the login
     * view.
     */
    public void logOut() {
        requestQueue.getCache().clear();
        mOAuth.deleteTokens();
    }

    /**
     * Does not attempt to refresh the token if expired.
     * Only used by the network portion.
     *
     * @return accesstoken from storage or memory
     */
    public String getAcessToken() {
        return mOAuth.getAccessToken();
    }

    /**
     * Refreshes the access token. Acquires a new refresh token.
     * Stores both the tokens in memory and on disk.
     *
     * @param code      RefreshToken or AuthorizationCode.
     * @param grantType use GRANT_TYPE string from oAuthHandler
     * @param callback  should implement Callback.onSuccess() at the least.
     */
    public void refreshToken(String code, String grantType, Callback callback) {
        mOAuth.getAccessToken(code, grantType, callback);
    }

    /**
     * Refreshes the access token. Acquires a new refresh token.
     * Stores both the tokens in memory and on disk.
     *
     * @param code      RefreshToken or AuthorizationCode.
     * @param grantType use GRANT_TYPE string from oAuthHandler
     */
    public void refreshToken(String code, String grantType) {
        mOAuth.getAccessToken(code, grantType);
    }

    /**
     * If AccessToken is timed out the method will attempt to renew it.
     * @return true if valid access token or false otherwise.
     */
    public boolean checkLogIn() {
        return mOAuth.isLoggedIn();
    }

    /**
     *if AccessToken is timed out the method will not attempt to renew it.
     * @return true if logged in or false otherwise.
     */
    public boolean checkTokensAtStartup(){
        return mOAuth.hasTokens();
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    /**
     * adds a request to the outgoing networkt
     *
     * @param req request to be added.
     * @param tag identifying tag. Used in case of cancelRequestQueue(tag).
     * @param <T> Type of request to be added.
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(tag);
        requestQueue.add(req);
    }

    /**
     * cancels all outgoing requests with the approprate tag.
     *
     * @param tag of the requests to be canceled. Specified in addToRequestQueue
     */
    public void cancelRequestQueue(String tag) {
        requestQueue.cancelAll(tag);
    }

    /**
     * Changes the project currently used as the basis for the app.
     * Ramification for what network calls are used.
     *
     * @param project that is selected by user.
     */
    public void setActiveProject(@NonNull Project project) {
        mActiveProject = project;
        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ProjectId", project.getProjectId());
        editor.putString("Name", project.getName());
        editor.putString("BimyncProjectId", project.getBimsyncProjectId());
        editor.putString("BimsyncProjectName", project.getBimsyncProjectName());
        editor.apply();
    }

    /**
     * Acquires the ActiveProject. Loads it from storage if needed.
     * @return ActiveProject.
     */
    public @Nullable Project getActiveProject() {
        if (mActiveProject == null) {
            SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
            String projectId = preferences.getString("ProjectId", null);
            String Name = preferences.getString("Name", null);
            String BimSyncProjectId = preferences.getString("BimyncProjectId", null);
            String BimSyncProjetName = preferences.getString("BimsyncProjectName", null);

            if (projectId != null && Name != null && BimSyncProjectId != null && BimSyncProjetName != null)
                mActiveProject = new Project(projectId, BimSyncProjetName, BimSyncProjectId, Name);
        }

        return mActiveProject;
    }
}
