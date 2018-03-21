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

public class ProjectEntityManager implements ProjectsFragmentInterface.FragmentProjectListener {

    private BimApp mContext;

    public ProjectEntityManager(BimApp context) {
        mContext = context;
    }

    private class ProjectCallback implements Callback {

        ProjectsFragmentInterface mControllerCallback;

        public ProjectCallback(ProjectsFragmentInterface controllerCallback) {
            mControllerCallback = controllerCallback;
        }

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
            gotProjects(projects, mControllerCallback);

        }
    }

    @Override
    public void gotProjects(List<Project> projects, ProjectsFragmentInterface controllerCallback) {
        controllerCallback.setProjects(projects);
    }

    public void getProjects(ProjectsFragmentInterface controllerCallback) {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET, NetworkConnManager.APICall.GETProjects,
                new ProjectCallback(controllerCallback), null);
    }

    public void getProject(String projectId) {

    }
}
