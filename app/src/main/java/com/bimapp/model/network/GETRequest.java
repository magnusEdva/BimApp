package com.bimapp.model.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bimapp.BimApp;
import com.bimapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Provider of a very general GET method that can be accessed through NetworkConnManager.
 */

public class GETRequest {


    protected static void GET(final BimApp mContext, final NetworkConnManager.JSONTypes responseType,
                              NetworkConnManager.APICall call, final Callback callback) {

        String url = call.getURL();
        StringRequest getUserRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (responseType == NetworkConnManager.JSONTypes.ARRAY)
                            onResponseArray(response, callback);
                        else if (responseType == NetworkConnManager.JSONTypes.OBJECT)
                            onResponseObject(response, callback);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.toString());
                        error.printStackTrace();
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
                headers.put("Accept", "application/json");

                return headers;
            }
        };

        mContext.add(getUserRequest);

    }

    private static void onResponseArray(String response, Callback callback) {
        JSONArray array;
        try {
            array = new JSONArray(response);
            callback.onSuccess(array);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void onResponseObject(String response, Callback callback) {
        JSONObject object;
        try {
            object = new JSONObject(response);
            callback.onSuccess(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

