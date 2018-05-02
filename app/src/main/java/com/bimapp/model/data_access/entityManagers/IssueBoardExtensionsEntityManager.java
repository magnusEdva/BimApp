package com.bimapp.model.data_access.entityManagers;

import android.util.Log;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.model.data_access.DataProvider;
import com.bimapp.model.data_access.network.APICall;
import com.bimapp.model.data_access.network.Callback;
import com.bimapp.model.data_access.network.NetworkConnManager;
import com.bimapp.model.entity.IssueBoardExtensions;
import com.bimapp.model.entity.Project;

import org.json.JSONException;
import org.json.JSONObject;

import static com.bimapp.model.data_access.DataProvider.PROJECT_TABLE;

public class IssueBoardExtensionsEntityManager {

    public interface IssueBoardExtensionsProjectCallback {
        public interface IssueBoardExtensionsProjectListener{
            public void getExtensions(IssueBoardExtensionsProjectCallback callback);
        }
        public void setExtensions(IssueBoardExtensions issueBoardExtensions);

    }

    private BimApp mContext;

    private ProjectDBHandler handler;

    public IssueBoardExtensionsEntityManager(BimApp mContext) {
        this.mContext = mContext;
        handler = new ProjectDBHandler(mContext.getContentResolver());
    }

    /**
     * Inner lass which implements Callback from {@link com.bimapp.model.data_access.network.NetworkConnManager}.
     */
    public class IssueBoardExtensionsCallback implements Callback<String>{

        IssueBoardExtensionsProjectCallback mProjectCallback;
        Project mProject;
        public IssueBoardExtensionsCallback(IssueBoardExtensionsProjectCallback issueBoardExtensionsCallback,
                                            Project project){
            mProjectCallback = issueBoardExtensionsCallback;
            mProject = project;
        }

        @Override
        public void onError(String response) {
            Log.d("IssueBoardExtensions", "Error on getting IssueBoardExtensions.\n"
                    + response);
            mProjectCallback.setExtensions(null);
        }

        @Override
        public void onSuccess(String JSONResponse) {
            Log.d("IssueBoardExtensions", "Successfully got IssueBoardExtensions." );
            IssueBoardExtensions issueBoardExtensions = null;
            try {
                JSONObject jsonObject = new JSONObject(JSONResponse);
                issueBoardExtensions = new IssueBoardExtensions(jsonObject);
                mProject.setIssueBoardExtensions(issueBoardExtensions);
                handler.startInsert(1,null, DataProvider.ParseUri(PROJECT_TABLE),mProject.getContentValues());
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
                , new IssueBoardExtensionsCallback( controllerCallback, project)
                ,null);
    }
}
