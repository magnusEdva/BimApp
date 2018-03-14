package com.bimapp.model.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bimapp.BimApp;
import com.bimapp.model.entity.Project;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Håkon on 14.03.2018.
 */

public class GetUser {


    public static void getUser(final BimApp mContext, final Callback callback) {


        String url = "https://api.bimsync.com/v2/projects"; //TODO Move this to strings XML
        StringRequest getUserRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray projects;
                        String name;
                        try {
                            projects= new JSONArray(response);
                            callback.onSuccess(projects);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Something happened", error.toString());
                        error.printStackTrace();
                    }
                }
        ) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization" , "Bearer " + mContext.getAcessToken());
                headers.put("Accept", "application/json");

                return headers;
            }
        };

        mContext.add(getUserRequest);

    }
}

