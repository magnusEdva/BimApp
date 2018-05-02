package com.bimapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArraySet;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bimapp.model.data_access.AppDatabase;
import com.bimapp.model.data_access.entityManagers.LogOutHelper;
import com.bimapp.model.data_access.network.Callback;
import com.bimapp.model.data_access.network.oauth.OAuthHandler;
import com.bimapp.model.entity.IssueBoardExtensions;
import com.bimapp.model.entity.Project;
import com.bimapp.model.entity.User;
import com.bimapp.model.entity.Viewpoint;

import java.util.ArrayList;
import java.util.HashSet;

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
     * This is the currently active user. Is set when you log in,
     * and is set to null when you log out.
     */
    private User mCurrentUser;
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
        Viewpoint.Snapshot.dir = this.getFilesDir().getPath();
        requestQueue = Volley.newRequestQueue(this);
        mOAuth = new OAuthHandler(this);
        checkTokensAndRefresh();
    }

    public OAuthHandler getMOAuth() {
        return mOAuth;
    }

    /**
     * deletes all tokens and cache from storage. After calling this, the presenter should change to the login
     * view. Also empties the offline storage.
     */
    public void logOut() {
        requestQueue.getCache().clear();
        mOAuth.deleteTokens();
        mCurrentUser = null;
        new LogOutHelper(AppDatabase.getInstance(this)).execute();
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
     *if AccessToken is timed out the method will attempt to renew it.
     * @return true if logged in or false otherwise.
     */
    public boolean checkTokensAndRefresh(){
        try {
            return mOAuth.hasTokens();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
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
     * cancels all outgoing requests with the appropriate tag.
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
        editor.putStringSet("topic_type", new HashSet<>(project.getIssueBoardExtensions().getTopicType()));
        editor.putStringSet("topic_status", new HashSet<String>(project.getIssueBoardExtensions().getTopicStatus()));
        editor.putStringSet("user_id_type", new HashSet<String>(project.getIssueBoardExtensions().getUserIdType()));
        editor.putStringSet("topic_label", new HashSet<String>(project.getIssueBoardExtensions().getTopicLabel()));
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
            IssueBoardExtensions issueBoardExtensions = null; // new IssueBoardExtensions(); // Default constructor returns default set of extensions
            if (projectId != null) {
                ArrayList<String> topic_type = new ArrayList<>(preferences.getStringSet("topic_type", IssueBoardExtensions.defaultType()));
                ArrayList<String> topic_status = new ArrayList<>(preferences.getStringSet("topic_status", IssueBoardExtensions.defaultStatus()));
                ArrayList<String> user_id_type = new ArrayList<>(preferences.getStringSet("user_id_type", new ArraySet<String>()));
                ArrayList<String> topic_label = new ArrayList<>(preferences.getStringSet("topic_label", new ArraySet<String>()));
                issueBoardExtensions = new IssueBoardExtensions(topic_type, topic_status, user_id_type, topic_label);
            }
            if (projectId != null && Name != null && BimSyncProjectId != null && BimSyncProjetName != null && issueBoardExtensions != null)
                mActiveProject = new Project(projectId, BimSyncProjetName, BimSyncProjectId, Name, issueBoardExtensions);
        }

        return mActiveProject;
    }

    public void setCurrentUser(User user) {
        mCurrentUser = user;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }
}
