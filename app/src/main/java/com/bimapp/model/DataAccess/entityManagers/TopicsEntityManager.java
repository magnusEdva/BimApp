package com.bimapp.model.DataAccess.entityManagers;

import android.util.Log;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.NewTopicFragmentInterface;
import com.bimapp.controller.interfaces.TopicFragmentInterface;
import com.bimapp.controller.interfaces.TopicsFragmentInterface;
import com.bimapp.model.entity.EntityListConstructor;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.DataAccess.network.APICall;
import com.bimapp.model.DataAccess.network.Callback;
import com.bimapp.model.DataAccess.network.NetworkConnManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A class that handles acquiring topics from the API for the presenter.
 */

public class TopicsEntityManager implements TopicsFragmentInterface.FragmentTopicsListener, NewTopicFragmentInterface.NewTopicFragmentListener {

    private BimApp mContext;
    private NewTopicFragmentInterface mListener;


    /**
     * Constructor for the getTopics fragment
     *
     * @param context
     */
    public TopicsEntityManager(BimApp context) {
        mContext = context;
    }

    /**
     * Constructor for postTopics fragment
     *
     * @param context
     * @param listener
     */
    public TopicsEntityManager(BimApp context, NewTopicFragmentInterface listener) {
        mContext = context;
        mListener = listener;
    }

    /**
     * Callback method from {@link }
     *
     * @param topic The topic you want to post
     */
    @Override
    public void postTopic(Topic topic) {
        postTopic(mListener, topic);
    }

    /**
     * Method to get topics from the server
     *
     * @param controllerCallback This is where the onSuccess/onError methods must be implemented
     */
    public void getTopics(TopicsFragmentInterface controllerCallback) {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET,
                APICall.GETTopics(mContext.getActiveProject()),
                new TopicsEntityManager.TopicsCallback(controllerCallback), null);
    }

    /**
     * Method to post topics to the server
     *
     * @param controllerCallback This is where the onSuccess/onError methods must be implemented
     * @param topic              The topic you want to post
     */
    public void postTopic(NewTopicFragmentInterface controllerCallback, Topic topic) {
        NetworkConnManager.networkRequest(mContext, Request.Method.POST,
                APICall.POSTTopics(mContext.getActiveProject()), new TopicPostCallback(controllerCallback), topic);

    }

    /**
     * Method to update a topic
     *
     * @param controllerCallback This is where the onSuccess/onError methods must be implemented
     * @param topic              the topic you wish to update
     */
    public void putTopic(TopicFragmentInterface controllerCallback, Topic topic) {
        NetworkConnManager.networkRequest(mContext, Request.Method.PUT,
                APICall.PUTTopic(mContext.getActiveProject(), topic), new putTopicsCallback(controllerCallback), topic);
    }
    /**
     * Inner class which handles the Callbacks from Volley on a postTopic request
     */
    private class TopicPostCallback implements Callback<String> {

        NewTopicFragmentInterface mTopicsFragmentInterface;

        public TopicPostCallback(NewTopicFragmentInterface callback) {
            mTopicsFragmentInterface = callback;
        }

        @Override
        public void onError(String response) {
            Log.d("TopicsEntityManager", response);
            Topic t = null;
            makeToast(false, t);
        }

        @Override
        public void onSuccess(String JSONResponse) {
            Log.d("TopicsEntityManager", "Successfully posted topic to server");

            JSONObject object = null;
            try {
                object = new JSONObject(JSONResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (object != null) {
                Topic topic = new Topic(object);
                makeToast(true, topic);
            }
        }

        public void makeToast(boolean success, Topic topic) {
            mListener.postedTopic(success, topic);
        }
    }


    /**
     * Inner class which handles the callbacks from Volley on a getTopics request
     */
    private class TopicsCallback implements Callback<String> {

        TopicsFragmentInterface mControllerCallback;

        public TopicsCallback(TopicsFragmentInterface controllerCallback) {
            mControllerCallback = controllerCallback;
        }

        @Override
        public void onError(String response) {
              if(response != null)  Log.d("getTopicsError", response);
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

    /**
     * Inner class which handles the callbacks from Volley on a putTopics request
     */
    private class putTopicsCallback implements Callback<String> {

        TopicFragmentInterface mControllerCallback;

        public putTopicsCallback(TopicFragmentInterface controllerCallback) {
            mControllerCallback = controllerCallback;
        }

        @Override
        public void onError(String response) {
            Log.d("TopicsEntityManager", response);
            }

        @Override
        public void onSuccess(String response) {
            Log.d("PUTCOMMENT:", response);
        }
    }


}
