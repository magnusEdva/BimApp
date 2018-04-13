package com.bimapp.model.network;

import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bimapp.BimApp;
import com.bimapp.R;
import com.bimapp.model.entity.Entity;
import com.bimapp.model.network.oauth.OAuthHandler;

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
                method, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onResponseString(response, callback);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onErrorResponse(error);
                        error.printStackTrace();
                        mContext.refreshToken(mContext.getAcessToken(), OAuthHandler.GRANT_TYPE_REFRESH_TOKEN);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + mContext.getAcessToken());
                headers.put("Accept", "application/json");

                return headers;
            }

            /**
             * parses Server errors into plain text.
             * @param volleyError Error that is sent. in byte.
             * @return error with actual string text.
             */
            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                    volleyError = error;
                }
                Log.d("ParseNetworkError", volleyError.getMessage());
                return volleyError;
            }
        };

        mContext.addToRequestQueue(getUserRequest, url);

    }

    static void POST(final BimApp mContext, final int method, String url,
                     final Callback callback, final Entity params) {

        JsonObjectRequest getUserRequest = new JsonObjectRequest(url, params.getJsonParams(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response.toString());
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        callback.onError(error.getMessage());

                    }
                }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/json";

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + mContext.getAcessToken());
                return headers;
            }


            /**
             * parses Server errors into plain text.
             * @param volleyError Error that is sent. in byte.
             * @return error with actual string text.
             */
            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                    volleyError = error;
                }
                Log.d("ParseNetworkError", volleyError.getMessage());
                return volleyError;
            }
        };

        mContext.addToRequestQueue(getUserRequest, url);

    }

    private static void onResponseString(String response, Callback callback) {
        callback.onSuccess(response);
    }

}

