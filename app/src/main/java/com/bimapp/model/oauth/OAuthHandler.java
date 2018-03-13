package com.bimapp.model.oauth;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bimapp.APIkey;
import com.bimapp.BimApp;
import com.bimapp.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible for handling all authentication using oAuth, namely getting the Authorization Code, getting the access token and the refresh token
 */
public class OAuthHandler {

    BimApp mContext;

    public OAuthHandler(Context context){
        mContext = (BimApp) context;
    }

    /**
     * Always fetches a new AccessToken
     * @return
     */
    public String getAccessToken(final String code, final OAuthCallback callback){


            String url = "https://api.bimsync.com/oauth2/token"; //TODO Move this to strings XML
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                callback.onSuccessResponse(response);

                                Log.d("Access Token","???");
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            callback.onErrorResponse(error);
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

            mContext.add(postRequest);
        return "";
    }

    public String getoAuthUrI(){
        StringBuilder URI = new StringBuilder();
        URI.append(mContext.getText(R.string.BimSyncURL)); //mContext.getText(R.string.BimSyncURL)"http://10.0.0.8:8089/"
        URI.append(mContext.getText(R.string.oAuth0));
        URI.append(mContext.getText(R.string.oAuthClientId));
        URI.append(APIkey.Client_id);
        URI.append(mContext.getText(R.string.oAuthRedirectURI));
        URI.append(mContext.getText(R.string.oAuthEnd));
        return URI.toString();
    }

    public void launchBrowser(){
        String url = getoAuthUrI();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(mContext, Uri.parse(url));
    }
}
