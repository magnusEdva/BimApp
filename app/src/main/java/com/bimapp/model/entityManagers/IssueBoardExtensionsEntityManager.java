package com.bimapp.model.entityManagers;

import android.util.Log;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.model.entity.IssueBoardExtensions;
import com.bimapp.model.entity.Project;
import com.bimapp.model.network.APICall;
import com.bimapp.model.network.Callback;
import com.bimapp.model.network.NetworkConnManager;

import org.json.JSONException;
import org.json.JSONObject;

public class IssueBoardExtensionsEntityManager {

    public interface IssueBoardExtensionsProjectCallback {
        public interface IssueBoardExtensionsProjectListener{
            public void getExtensions(IssueBoardExtensionsProjectCallback callback);
        }
        public void setExtensions(IssueBoardExtensions issueBoardExtensions);

    }

    private BimApp mContext;

    public IssueBoardExtensionsEntityManager(BimApp mContext) {
        this.mContext = mContext;
    }

    /**
     * Inner lass which implements Callback from {@link com.bimapp.model.network.NetworkConnManager}.
     */
    public class IssueBoardExtensionsCallback implements Callback<String>{

        IssueBoardExtensionsProjectCallback mProjectCallback;
        public IssueBoardExtensionsCallback(IssueBoardExtensionsProjectCallback issueBoardExtensionsCallback){
            mProjectCallback = issueBoardExtensionsCallback;
        }

        @Override
        public void onError(String response) {
            Log.d("IssueBoardExtensions", "Error on getting IssueBoardExtensions.\n"
                    + response);
        }

        @Override
        public void onSuccess(String JSONResponse) {
            Log.d("IssueBoardExtensions", "Successfully got IssueBoardExtensions." );
            IssueBoardExtensions issueBoardExtensions = null;
            try {
                JSONObject jsonObject = new JSONObject(JSONResponse);
                issueBoardExtensions = new IssueBoardExtensions(jsonObject);
            } catch (JSONException e){
                e.printStackTrace();
            }
            mProjectCallback.setExtensions(issueBoardExtensions);
        }
    }

    public void getIssueBoardExtensions(Project project, IssueBoardExtensionsProjectCallback controllerCallback){
        NetworkConnManager.networkRequest(mContext
                , Request.Method.GET
                , APICall.GETIssueBoardExtensions(project)
                , new IssueBoardExtensionsCallback( controllerCallback)
                ,null);
    }
}
