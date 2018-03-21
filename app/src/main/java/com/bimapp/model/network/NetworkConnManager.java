package com.bimapp.model.network;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.model.entity.Entity;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Provider of all access to all API calls and methods.
 */

public class NetworkConnManager {


    private NetworkConnManager() {}

    /**
     * Sends a GET request using the @Param call URL. Data is provided to the callbacks onSuccess.
     *
     * @param context      Required to aqcuire tokens.
     * @param method       type of request.
     * @param call         the call to be executed. Found in NetworkConnManager.APICall
     * @param callback     implementation of the network.Callback interface.
     */
    static public void networkRequest(@NonNull BimApp context, @NonNull int method,
                                      @NonNull APICall call, @NonNull Callback callback, @Nullable Entity params) {
        if (params == null && method != Request.Method.GET) {
            //TODO fix
        }else if(context.checkLogIn()){
            BimAppRequest.GET(context, method, call, new networkCallback(callback), params);
        } else
            try {
                Thread.sleep(100);
                Log.d("asleep","kindoff");
                networkRequest(context, method, call,callback, params);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

    }

    public static class networkCallback implements Callback{
        Callback otherCall;
        public networkCallback(Callback otherCallback){
            otherCall = otherCallback;
        }

        @Override
        public void onError(String response) {
            otherCall.onError(response);
        }

        @Override
        public void onSuccess(String response) {
            otherCall.onSuccess(response);
        }

    }

    /**
     * lists all the APIcalls supported by the app.
     * Also includes correct URLs
     */
    public enum APICall {
        GETProjects("https://api.bimsync.com/v2/projects"),
        GETUser("https://bcf.bimsync.com/bcf/beta/current-user"),
        GETTopics("https://bcf.bimsync.com/bcf/beta/projects/bb76d10d62c24bc18dda452e5d0fe6be/topics");


        private String mURL;

        APICall(String URL) {
            mURL = URL;
        }

        public String getURL() {
            return mURL;
        }
    }
}
