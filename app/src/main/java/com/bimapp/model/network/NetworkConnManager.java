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
     * @param url          the url to be executed. Found in APICall
     * @param callback     implementation of the network.Callback interface.
     */
    static public void networkRequest(@NonNull BimApp context, @NonNull int method,
                                      @NonNull String url, @NonNull Callback callback, @Nullable Entity params) {
        if (params == null && method != Request.Method.GET) {
            //TODO fix
        }else if(context.checkLogIn()){
            sendRequest(context, method, url, new networkCallback(callback), params);
        } else
            try {
                Thread.sleep(1000);
                Log.d("NetworkConnManager","Trying to get new code");
                networkRequest(context, method, url,callback, params);
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

    private static void sendRequest(BimApp context, int method, String url, Callback callback, Entity params){
        switch (method){
            case(Request.Method.GET):
                BimAppRequest.GET(context, method, url, new networkCallback(callback), params);
                break;
            case(Request.Method.POST):
                BimAppRequest.POST(context, method, url, new networkCallback(callback), params);
                break;
        }
    }
}
