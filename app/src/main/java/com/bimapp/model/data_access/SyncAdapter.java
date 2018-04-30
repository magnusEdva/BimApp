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

import com.bimapp.BimApp;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    // Global variables
    // Define a variable to contain a content resolver instance
    private final ContentResolver mContentResolver;

    private final AccountManager mAccountManager;

    private BimApp mContext;

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
        // Should somehow determine if server and local db is different


        // Get Projects/IssueBoards

        // Get Topics

        // Get Comments

        // Get ViewPoints

        // Determine what needs to be uploaded



    }
}