package com.bimapp.model.entityManagers;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.ProjectsFragmentInterface;
import com.bimapp.model.entity.EntityListConstructor;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.network.APICall;
import com.bimapp.model.network.Callback;
import com.bimapp.model.network.NetworkConnManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * A class that handles acquiring topics from the API for the presenter.
 */

public class TopicsEntityManager {

    private BimApp mContext;

    public TopicsEntityManager(BimApp context) {
        mContext = context;
    }

    private class TopicsCallback implements Callback {

        ProjectsFragmentInterface mControllerCallback;

        public TopicsCallback(ProjectsFragmentInterface controllerCallback) {
            mControllerCallback = controllerCallback;
        }

        @Override
        public void onError(String response) {
            // TODO Error handling

        }

        @Override
        public void onSuccess(String response) {
            List<Topic> topics = null;

            try {
                JSONArray jsonArray = new JSONArray(response);
                topics = EntityListConstructor.Topics(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Make project list
            // Callback to fragment with list
            //gotProjects(projects, mControllerCallback);

        }
    }


    public void gotTopics(List<Topic> topics) {
        //controllerCallback.setProjects(topics);
    }

    public void getTopics(ProjectsFragmentInterface controllerCallback) {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET,
                APICall.GETTopics(mContext.getActiveProject()),
                new TopicsEntityManager.TopicsCallback(controllerCallback), null);
    }

    public void getTopic(String topicId) {

    }
}
