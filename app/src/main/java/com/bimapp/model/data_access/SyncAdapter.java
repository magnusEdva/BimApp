package com.bimapp.model.data_access;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.model.data_access.network.APICall;
import com.bimapp.model.data_access.network.Callback;
import com.bimapp.model.data_access.network.NetworkConnManager;
import com.bimapp.model.entity.EntityListConstructor;
import com.bimapp.model.entity.Project;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {    // Global variables
    // Define a variable to contain a content resolver instance
    private final ContentResolver mContentResolver;

    private final AccountManager mAccountManager;

    private final BimApp mContext;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(context);
        mContext = (BimApp) context;
        Log.d("SyncAdapter", "Created SyncAdapter") ;
    }


    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();

        mAccountManager = AccountManager.get(context);
        mContext = (BimApp) context;
        Log.d("SyncAdapter", "Created SyncAdapter, Compat ");
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              SyncResult syncResult) {

        Log.d("SyncAdapter", "Started syncing");

        GetProjects();

        PostTopics();

    }

    private void PostTopics() {

        // Post unsynced topics

        // Then post ViewPoints if Comment has ViewPoint

        // Then post Comment
    }

    /**
     * Method to get projects (IssueBoards) from the server.
     *
     * Callback from the result of this methods call should implement getting
     */
    private void GetProjects() {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET, APICall.GETProjects(),new ProjectCallback()
                , null);
    }


    // Private classes implements Volley Callbacks for specific

    private class ProjectCallback implements Callback<String>{

        @Override
        public void onError(String response) {
            // TODO Error handling
            Log.d("SyncAdapter", "Error on callback. " + response);
        }

        @Override
        public void onSuccess(String response) {
            List<Project> projects = null;
            Log.d("SyncAdapter", "Got successful response");
            try {
                JSONArray jsonArray = new JSONArray(response);
                projects = EntityListConstructor.Projects(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (projects != null){
                for (Project project: projects){
                    mContentResolver.insert(DataProvider.ParseUri(DataProvider.PROJECT_TABLE),project.getContentValues());
                }
                Log.d("SyncAdapter", "Got projects");
            }
        }
    }
}