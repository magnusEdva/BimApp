package com.bimapp.model.DataAccess.network.oauth;

import com.android.volley.VolleyError;
import com.bimapp.model.DataAccess.network.Callback;

/**
 * Created by Håkon on 13.03.2018.
 */

public interface OAuthCallback {
    void onSuccessResponse(String result, Callback callback);
    void onErrorResponse(VolleyError errror);
}
