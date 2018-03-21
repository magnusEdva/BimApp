package com.bimapp.model.entityManagers;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.ProjectsFragmentrInterface;
import com.bimapp.model.entity.Project;
import com.bimapp.model.network.Callback;
import com.bimapp.model.network.NetworkConnManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Håkon on 20.03.2018.
 */

public class ProjectEntityManager implements ProjectsFragmentrInterface.ProjectsListener {

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

            }

            @Override
            public void onSuccess(String response) {
                List<Project> projects;

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Project p = new Project();

                    projects = Arrays.asList((Project[]) p.construct(jsonArray));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Make project list
                // Callback to fragment with list
                onProjectSelected("sdf");

            }

        };
    }


    @Override
    public void onProjectSelected(String projectId) {

    }

    public void getProjects() {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET, NetworkConnManager.APICall.GETProjects,callback,null);
    }

    public void getProject(String projectId){

    }
}
