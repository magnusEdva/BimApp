package com.bimapp.model.entityManagers;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.ProjectsFragmentInterface;
import com.bimapp.model.entity.EntityListConstructor;
import com.bimapp.model.entity.Project;
import com.bimapp.model.network.APICall;
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

    /**
     * Class which implements Callback from {@Link NetworkConnManager}.
     * Done to make it easier to see what this class does.
     * <p>
     * Does it actually make it easier to see what this class does?
     */
    private class ProjectCallback implements Callback {

        ProjectsFragmentInterface mControllerCallback;

        /**
         * Constructor
         *
         * @param controllerCallback is used to pass information to the fragment that wants the list of projects
         */
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
                projects = EntityListConstructor.Projects(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mControllerCallback.setProjects(projects);
        }
    }

    /**
     * The method that calls the {@Link NetworkConnManager} with a request for the API.
     *
     * @param controllerCallback is an interface that defines the methods for handling the responses from the {@Link NetworkConnManager}
     */
    public void getProjects(ProjectsFragmentInterface controllerCallback) {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET, APICall.GETProjects(),
                new ProjectCallback(controllerCallback), null);
    }

    /**
     * Unimplemented method. Should return a single project to the fragment.
     * @param projectId the Id of the wanted project
     */
    public void getProject(String projectId) {

    }
}
