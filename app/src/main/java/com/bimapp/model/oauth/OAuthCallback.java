package com.bimapp.model.oauth;

import com.android.volley.VolleyError;

/**
 * Created by Håkon on 13.03.2018.
 */

public interface OAuthCallback {
    void onSuccessResponse(String result);
    void onErrorResponse(VolleyError errror);
}
