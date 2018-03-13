package com.bimapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoggedIn extends AppCompatActivity {

    BimApp mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        mApplication = (BimApp) this.getApplicationContext();
        //String code = getIntent().getData().getQueryParameter("code");

        //getToken(code);
    }

    private void getToken(final String code){


        String url = "https://api.bimsync.com/oauth2/token";



        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Log.d("Access Token","???");
                        } catch (Exception e){
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
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<>();
                // the POST parameters:
                params.put("client_id", APIkey.Client_id);
                params.put("client_secret",APIkey.Secret_id);
                params.put("code", code );
                params.put("grant_type", "authorization_code");
                params.put("redirect_uri", "bimapp://oauthresponse");
                return params;
            }
            @Override
                    public String getBodyContentType()
            {
                return "application/x-www-form-urlencoded";

            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Accept", "application/json");

                return headers;
            }
        };

        mApplication.add(postRequest);
    }



}
