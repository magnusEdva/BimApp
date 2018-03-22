package com.bimapp.model.entityManagers;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.TopicsFragmentInterface;
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

public class TopicsEntityManager implements TopicsFragmentInterface.FragmentTopicsListener {

    private BimApp mContext;

    public TopicsEntityManager(BimApp context) {
        mContext = context;
    }

    private class TopicsCallback implements Callback {

        TopicsFragmentInterface mControllerCallback;

        public TopicsCallback(TopicsFragmentInterface controllerCallback) {
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
            mControllerCallback.setTopics(topics);
        }
    }

    public void getTopics(TopicsFragmentInterface controllerCallback) {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET,
                APICall.GETTopics(mContext.getActiveProject()),
                new TopicsEntityManager.TopicsCallback(controllerCallback), null);
    }

    //TODO Implement a method that lets the activity know what this fragment did!

}
