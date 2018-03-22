package com.bimapp.model.network;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bimapp.BimApp;
import com.bimapp.R;
import com.bimapp.model.entity.Entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Provider of a very general GET method that can be accessed through NetworkConnManager.
 */

class BimAppRequest {


    static void GET(final BimApp mContext, final int method, String url,
                    final Callback callback, @Nullable final Entity params) {

        StringRequest getUserRequest = new StringRequest(
                method,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onResponseString(response, callback);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return mContext.getString(R.string.url_encode);

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + mContext.getAcessToken());
                if(method == Method.GET)
                    headers.put("Accept", "application/json");
                else if(method == Method.POST)
                    headers.put("Content-type", "application/json");
                return headers;
            }
            @Override
            public Map<String,String> getParams(){
                Map<String, String> tempParams = null;
                if(params != null){
                    tempParams = new HashMap<>();
                    tempParams = params.getParams(tempParams);
                }
                return tempParams;
            }

            /**
             * parses Server errors into plain text.
             * @param volleyError Error that is sent. in byte.
             * @return error with actual string text.
             */
            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError){
                if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
                    VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                    volleyError = error;
                }
                Log.d("suck me", volleyError.getMessage());
                return volleyError;
            }
        };

        mContext.addToRequestQueue(getUserRequest, url);

    }

    private static void onResponseString(String response, Callback callback) {
            callback.onSuccess(response);
    }

}

