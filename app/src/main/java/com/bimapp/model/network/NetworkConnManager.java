package com.bimapp.model.network;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.model.entity.Entity;

/**
 * Provider of all access to all API calls and methods.
 */

public class NetworkConnManager {


    private NetworkConnManager() {
    }

    /**
     * Sends a GET request using the @Param call URL. Data is provided to the callbacks onSuccess.
     *
     * @param context      Required to aqcuire tokens.
     * @param method       type of request.
     * @param responseType expected JSON Type in response
     * @param call         the call to be executed. Found in NetworkConnManager.APICall
     * @param callback     implementation of the network.Callback interface.
     */
    static public void networkRequest(@NonNull BimApp context, @NonNull int method, @NonNull JSONTypes responseType,
                                      @NonNull APICall call, @NonNull Callback callback, @Nullable Entity params) {
        if (params == null && method != Request.Method.GET) {
            //TODO fix
        } else
            BimAppRequest.GET(context, method, responseType, call, callback, params);
    }

    public enum JSONTypes {
        OBJECT, ARRAY
    }

    public enum APICall {
        GETProjects("https://api.bimsync.com/v2/projects"),
        GETUser("https://api.bimsync.com/v2/user"),
        GETTopics("...");

        private String mURL;

        APICall(String URL) {
            mURL = URL;
        }

        public String getURL() {
            return mURL;
        }
    }
}
