package com.bimapp.model.oauth;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bimapp.APIkey;
import com.bimapp.BimApp;
import com.bimapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible for handling all authentication using oAuth, namely getting the Authorization Code, getting the access token and the refresh token
 */
public class OAuthHandler implements OAuthCallback {

    public final static String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    public final static String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";

    BimApp mContext;

    public OAuthHandler(Context context) {
        mContext = (BimApp) context;
    }

    /**
     * Aqcuires an access token and a refresh Token. Uses OAuthCallback implemented by OAuthHandler
     * Requires that @Param grantType is either "authorization_code" or "refresh_token". And that @Param
     * code contains the appropriate value.
     *
     * @param code      @NonNull
     * @param grantType either GRANT_TYPE_AUTHORIZATION_CODE or GRANT_TYPE_REFRESH_TOKEN
     */
    public void getAccessToken(@NonNull final String code, @NonNull final String grantType) {


        String url = mContext.getString(R.string.api_token);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            OAuthHandler.this.onSuccessResponse(response);

                            Log.d("Access Token", "Successfully got an access token");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        OAuthHandler.this.onErrorResponse(error);
                        Log.d("Something happened", error.toString());
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("client_id", APIkey.Client_id);
                params.put("client_secret", APIkey.Secret_id);
                params.put("grant_type", grantType);
                params.put("redirect_uri", "bimapp://oauthresponse");

                if (grantType == GRANT_TYPE_AUTHORIZATION_CODE)
                    params.put("code", code);
                else if (grantType == GRANT_TYPE_REFRESH_TOKEN)
                    params.put("refresh_token", code);

                return params;
            }

            @Override
            public String getBodyContentType() {
                return mContext.getString(R.string.url_encode);

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Accept", "application/json");

                return headers;
            }
        };

        postRequest.setShouldRetryServerErrors(true);
        mContext.add(postRequest, "token");

    }

    public String getOAuthUri() {
        StringBuilder URI = new StringBuilder();
        URI.append(mContext.getText(R.string.BimSyncURL)); //mContext.getText(R.string.BimSyncURL)"http://10.0.0.8:8089/"
        URI.append(mContext.getText(R.string.oAuth0));
        URI.append(mContext.getText(R.string.oAuthClientId));
        URI.append(APIkey.Client_id);
        URI.append(mContext.getText(R.string.oAuthRedirectURI));
        URI.append(mContext.getText(R.string.oAuthEnd));
        return URI.toString();
    }

    public void launchBrowser() {
        String url = getOAuthUri();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        customTabsIntent.launchUrl(mContext, Uri.parse(url));
    }

    @Override
    public void onSuccessResponse(String result) {
        JSONObject response;

        String access_token;
        String refresh_token;
        String token_type;
        String expires_in;
        try {
            response = new JSONObject(result);
            access_token = response.getString("access_token");
            refresh_token = response.getString("refresh_token");
            token_type = response.getString("token_type");
            expires_in = response.getString("expires_in");
            mContext.storeAccesToken(access_token, Integer.parseInt(expires_in));
            mContext.storeRefreshToken(refresh_token);
        } catch (JSONException j) {
            j.printStackTrace();
        }


    }

    @Override
    public void onErrorResponse(VolleyError error) {

        Log.d("VolleyError", error.getMessage());
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(mContext,
                    mContext.getString(R.string.errorNetworkTimeout),
                    Toast.LENGTH_LONG).show();
        } else if (error instanceof AuthFailureError) {
            launchBrowser();
        } else if (error instanceof ServerError) {
            launchBrowser();
        } else if (error instanceof NetworkError) {
            //TODO
        } else if (error instanceof ParseError) {
            //TODO
        }
    }
}
