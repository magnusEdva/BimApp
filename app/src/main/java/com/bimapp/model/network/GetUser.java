package com.bimapp.model.network;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bimapp.APIkey;
import com.bimapp.BimApp;
import com.bimapp.model.oauth.OAuthHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Håkon on 14.03.2018.
 */

public class GetUser {


    public static void getUser(final BimApp mContext) {


        String url = "https://api.bimsync.com/v2/user"; //TODO Move this to strings XML
        StringRequest getUserRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject user;
                        String name;
                        try {
                            user = new JSONObject(response);
                            name = user.getString("name");
                            Toast.makeText(mContext, name, Toast.LENGTH_LONG ).show();

                            Log.d("Access Token", "???");
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

