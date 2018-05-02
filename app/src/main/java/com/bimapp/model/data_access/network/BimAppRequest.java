package com.bimapp.model.data_access.network;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bimapp.BimApp;
import com.bimapp.model.entity.entity;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Provider of a very general GET method that can be accessed through NetworkConnManager.
 */

class BimAppRequest {


    static void GET(final BimApp mContext, final int method, String url,
                    final Callback callback, @Nullable final entity params) {

        StringRequest getUserRequest = new StringRequest(
                method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String UTF8String = new String(response.getBytes("ISO-8859-1"), "UTF8");
                    onResponseString(UTF8String, callback);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
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
                return volleyError;
            }

            @Override
            protected String getParamsEncoding() {
                return "UTF-8";
            }
        };

        mContext.addToRequestQueue(getUserRequest, url);

    }

    static void POST(final BimApp mContext, final int method, String url,
                     final Callback callback, final entity params) {

        JsonObjectRequest getUserRequest = new JsonObjectRequest(method, url, params.getJsonParams(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String responseString = response.toString();
                try {

                    String UTF8String = new String(responseString.getBytes("ISO-8859-1"), "UTF8");
                    callback.onSuccess(UTF8String);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                return volleyError;
            }

            @Override
            protected String getParamsEncoding() {
                return "UTF-8";
            }
        };

        mContext.addToRequestQueue(getUserRequest, url);

    }


    static void GETImage(final BimApp mContext, final int method, String url,
                         final Callback<Bitmap> callback) {
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        callback.onSuccess(response);
                    }
                },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());

                    }
                }

        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + mContext.getAcessToken());
                return headers;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                    volleyError = error;
                }
                return volleyError;
            }

            @Override
            protected String getParamsEncoding() {
                return "UTF-8";
            }
        };
        mContext.addToRequestQueue(imageRequest, url);
    }

    private static void onResponseString(String response, Callback callback) {
        callback.onSuccess(response);
    }

}

