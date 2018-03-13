package com.bimapp.model.oauth;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;

import com.bimapp.APIkey;
import com.bimapp.BimApp;
import com.bimapp.R;

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
}
