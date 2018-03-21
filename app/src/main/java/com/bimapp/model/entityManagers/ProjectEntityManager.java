package com.bimapp.model.entityManagers;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.ProjectsFragmentInterface;
import com.bimapp.model.entity.EntityListConstructor;
import com.bimapp.model.entity.Project;
import com.bimapp.model.network.Callback;
import com.bimapp.model.network.NetworkConnManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by Håkon on 20.03.2018.
 */

public class ProjectEntityManager implements ProjectsFragmentInterface.ProjectsListener {

    private BimApp mContext;
    // Callback from the network
    private Callback callback;

    public ProjectEntityManager(BimApp context){
        mContext = context;
        callback = createCallback();

    }

    private Callback createCallback() {
        return new Callback() {
            @Override
            public void onError(String response) {
                // TODO Error handling

            }

            @Override
            public void onSuccess(String response) {
                List<Project> projects = null;

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    projects = EntityListConstructor.constructProjects(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Make project list
                // Callback to fragment with list
                gotProjects(projects);

            }

        };
    }


    @Override
    public void gotProjects(List<Project> projects) {

    }

    public void getProjects() {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET, NetworkConnManager.APICall.GETProjects,callback,null);
    }

    public void getProject(String projectId){

    }
}
