package com.bimapp.model.DataAccess.network;


import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.WelcomeActivity;
import com.bimapp.model.entity.entity;

/**
 * Provider of all access to all API calls and methods.
 */

public class NetworkConnManager {


    private NetworkConnManager() {}

    /**
     * Sends a request using the @aram call URL. Data is provided to the callbacks onSuccess.
     *
     * @param context      Required to acquire tokens.
     * @param method       type of request. From Volley.Request + 11 for acquiring an Image
     * @param url          the url to be executed. Found in APICall
     * @param callback     implementation of the network.Callback interface.
     */
    static public void networkRequest(@NonNull BimApp context, @NonNull int method,
                                      @NonNull String url, @NonNull Callback callback, @Nullable entity params) {

        if(context.checkLogIn()){
            sendRequest(context, method, url, callback, params);
        } else{
            Intent intent = new Intent(context, WelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
    }

    public static class networkCallback implements Callback<String>{
        Callback<String> otherCall;
         networkCallback(Callback<String> otherCallback){
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

    public static class imageCallback implements Callback<Bitmap>{
        Callback<Bitmap> otherCall;
         imageCallback(Callback<Bitmap> otherCallback){
            otherCall = otherCallback;
        }

        @Override
        public void onError(String response) {
            otherCall.onError(response);
        }

        @Override
        public void onSuccess(Bitmap response) {
            otherCall.onSuccess(response);
        }
    }


    private static void sendRequest(BimApp context, int method, String url, Callback callback, entity params){
        switch (method){
            case(Request.Method.GET):
                BimAppRequest.GET(context, method, url, new networkCallback(callback), params);
                break;
            case(Request.Method.POST):
                BimAppRequest.POST(context, method, url, new networkCallback(callback), params);
                break;
            case(Request.Method.PUT):
                BimAppRequest.POST(context, method, url, new networkCallback(callback), params);
                break;
            case(11):
                BimAppRequest.GETImage(context, method, url, new imageCallback(callback));
                break;

        }
    }
}
