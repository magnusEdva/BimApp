package com.bimapp.model.oauth;

import android.content.Context;

import com.bimapp.BimApp;

/**
 * Something. Something, oauth.
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
    public String getAccessToken(){
        mContext.getAuthorizatonCode();

        return "";
    }
}
