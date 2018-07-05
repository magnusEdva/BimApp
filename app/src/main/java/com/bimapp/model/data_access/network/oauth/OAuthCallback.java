package com.bimapp.model.data_access.network.oauth;

import com.android.volley.VolleyError;
import com.bimapp.model.data_access.network.Callback;

/**
 * Created by Håkon on 13.03.2018.
 */

public interface OAuthCallback {
    void onSuccessResponse(String result, Callback callback);
    void onErrorResponse(VolleyError errror);
}
