package com.bimapp.model.data_access.entityManagers;

import android.util.Log;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.ProjectsFragmentInterface;
import com.bimapp.model.data_access.DataProvider;
import com.bimapp.model.data_access.network.APICall;
import com.bimapp.model.data_access.network.Callback;
import com.bimapp.model.data_access.network.NetworkConnManager;
import com.bimapp.model.entity.EntityListConstructor;
import com.bimapp.model.entity.Project;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by Håkon on 20.03.2018.
 */

public class ProjectEntityManager implements ProjectsFragmentInterface.FragmentProjectListener, ProjectsFragmentInterface {

    private BimApp mContext;

    private ProjectDBHandler handler;

    public ProjectEntityManager(BimApp context) {
        mContext = context;
        handler = new ProjectDBHandler(context.getContentResolver());
    }

    @Override
    public void setProjects(List<Project> projects) {

    }

    /**
     * Class which implements Callback from {@Link NetworkConnManager}.
     * Done to make it easier to see what this class does.
     * <p>
     * Does it actually make it easier to see what this class does?
     */
    private class ProjectCallback implements Callback<String> {

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

            Log.d("ProjectEntityManager", response);

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
            for(Project p : projects)
                handler.startInsert(1,mControllerCallback, DataProvider.ParseUri(DataProvider.PROJECT_TABLE),p.getContentValues() );
            mControllerCallback.setProjects(projects);

        }
    }

    /**
     * The method that calls the {@Link NetworkConnManager} with a request for the API.
     *
     * @param controllerCallback is an interface that defines the methods for handling the responses from the {@Link NetworkConnManager}
     */
    public void getProjects(ProjectsFragmentInterface controllerCallback) {
        handler.startQuery(1,controllerCallback, DataProvider.ParseUri(DataProvider.PROJECT_TABLE),
                null,null,null,null);
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
