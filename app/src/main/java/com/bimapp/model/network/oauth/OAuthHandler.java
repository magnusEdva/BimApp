package com.bimapp.model.network.oauth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.bimapp.model.network.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Class responsible for handling all authentication using oAuth,
 * namely getting the Authorization Code, getting the access token and the refresh token
 */
public class OAuthHandler {

    public final static String OAUTH_REQUEST_CODE = "code";
    /**
     * These strings are used to tell the class what kind of the authentication is being used
     * at this time.
     */
    public final static String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    /**
     * These strings are used to tell the class what kind of the authentication is being used
     * at this time.
     */
    public final static String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
    /**
     * access key for finding the shared preference in which the oAuth tokens are stored.
     */
    private final static String SHARED_PREFS_KEY = "oAuth";
    /**
     * used to aqcuire application based data.
     */
    private BimApp mContext;
    /**
     * oAuth 2.0 acces token. Expires at the time provided by expiresAt.
     */
    private String accessToken;
    /**
     * oAuth 2.0 refresh token. Used to refres an expired access token.
     */
    private String refreshToken;
    /**
     * ms from 1970 when the access token will expire.
     */
    private long expiresAt;

    /**
     * is 0 if there is no pending token refresh or 1 if there is.
     * set to 0 when the refresh token returns onSuccess().
     */
    private int refreshCycleCheck;

    /**
     * @param context a BimApp application
     */

    public OAuthHandler(BimApp context) {
        mContext = context;
        refreshCycleCheck = 0;
        accessToken = null;
        refreshToken = null;
        expiresAt = -1L;
    }

    /**
     * Aqcuires an access token and a refresh Token. Uses OAuthCallback implemented by OAuthHandler
     * Requires that @Param grantType is either "authorization_code" or "refresh_token". And that @Param
     * code contains the appropriate value.
     *
     * @param code      @NonNull
     * @param grantType either GRANT_TYPE_AUTHORIZATION_CODE or GRANT_TYPE_REFRESH_TOKEN
     * @param callback  used when acquiring the first token.
     */
    public void getAccessToken(@NonNull final String code, @NonNull final String grantType, @Nullable final Callback callback) {
        if (checkActiveRefresh())
            return;

        setActiveRefresh();


        String url = mContext.getString(R.string.api_token);
        StringRequest oAuthRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            new CallbackHandler().onSuccessResponse(response);
                            if (callback != null)
                                callback.onSuccess(response);
                            Log.d("Access Token", "Successfully got an access token");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            new CallbackHandler().onErrorResponse(error);
                            error.printStackTrace();
                        }
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

                if (GRANT_TYPE_AUTHORIZATION_CODE.equals(grantType))
                    params.put("code", code);
                else if (GRANT_TYPE_REFRESH_TOKEN.equals(grantType))
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
        oAuthRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        oAuthRequest.setShouldRetryServerErrors(true);
        mContext.addToRequestQueue(oAuthRequest, "token");

    }

    /**
     * return true if there is an active Refresh
     */
    public boolean checkActiveRefresh() {
        return (refreshCycleCheck != 0);
    }

    public void setActiveRefresh() {
        refreshCycleCheck++;
    }

    public void setFinishedRefresh() {
        refreshCycleCheck = 0;
    }

    /**
     * calls getAccessToken(String, String, null);
     *
     * @param code
     * @param grantType
     */
    public void getAccessToken(@NonNull final String code, @NonNull final String grantType) {
        getAccessToken(code, grantType, null);
    }

    private String getOAuthUri() {
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

    /**
     * effectively logs out.
     */
    public void deleteTokens() {
        accessToken = null;
        refreshToken = null;
        expiresAt = -1L;

        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.apply();
    }


    public void storeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;

        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("RefreshToken", refreshToken);
        edit.apply();
    }

    public String getRefreshToken() {
        if (refreshToken == null) {
            SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
            refreshToken = prefs.getString("RefreshToken", null);
        }
        return refreshToken;
    }

    private void storeAccesToken(String accessToken, int expiration) {
        this.expiresAt = System.currentTimeMillis() + (expiration * 1000);
        this.accessToken = accessToken;

        SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("AccessToken", accessToken);
        edit.putLong("ExpiresAt", expiresAt);
        edit.apply();
    }

    public String getAccessToken() {
        if (accessToken == null) {
            SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
            accessToken = prefs.getString("AccessToken", null);
        }
        return accessToken;
    }

    public long getExpiresAt() {
        if (expiresAt == -1L) {
            SharedPreferences prefs = mContext.getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);
            expiresAt = prefs.getLong("ExpiresAt", -1L);
        }
        return expiresAt;
    }

    private Boolean isValidAccessToken() {
        accessToken = getAccessToken();
        refreshToken = getRefreshToken();
        expiresAt = getExpiresAt();

        return accessToken != null && refreshToken != null
                && expiresAt >= System.currentTimeMillis() + 10000;

    }


    /**
     * Checks login under user
     *
     * @return whether the app has a valid token or not.
     */
    public boolean isLoggedIn() {
        if (isValidAccessToken()) {
            return true;
        } else if (getRefreshToken() == null) {
            return false;
        } else if (getRefreshToken() != null) {
            if (!checkActiveRefresh())
                getAccessToken(getRefreshToken(), OAuthHandler.GRANT_TYPE_REFRESH_TOKEN);
            return true;
        }
        return false;
    }

    /**
     * checks initialLogin. Used only at app creation.
     */
    public boolean hasTokens() {
        if (getRefreshToken() != null) {
            if (!checkActiveRefresh())
                getAccessToken(getRefreshToken(), GRANT_TYPE_REFRESH_TOKEN);
            return true;
        } else
            return false;

    }

    private class CallbackHandler implements OAuthCallback {
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

                storeAccesToken(access_token, Integer.parseInt(expires_in));
                storeRefreshToken(refresh_token);
                setFinishedRefresh();
            } catch (JSONException j) {
                j.printStackTrace();
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                error.printStackTrace();
            } else if (error instanceof AuthFailureError) {
                error.printStackTrace();
            } else if (error instanceof ServerError) {
                error.printStackTrace();
            } else if (error instanceof NetworkError) {
                error.printStackTrace();
            } else if (error instanceof ParseError) {
                error.printStackTrace();
            }
        }
    }


}
