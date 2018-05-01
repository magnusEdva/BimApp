package com.bimapp.model.data_access.entityManagers;

import android.util.Log;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.NewTopicFragmentInterface;
import com.bimapp.controller.interfaces.TopicFragmentInterface;
import com.bimapp.controller.interfaces.TopicsFragmentInterface;
import com.bimapp.model.data_access.AppDatabase;
import com.bimapp.model.data_access.DataProvider;
import com.bimapp.model.data_access.network.APICall;
import com.bimapp.model.data_access.network.Callback;
import com.bimapp.model.data_access.network.NetworkConnManager;
import com.bimapp.model.entity.EntityListConstructor;
import com.bimapp.model.entity.Topic;

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
    private TopicDBHandler handler;


    /**
     * Constructor for the getTopics fragment
     *
     * @param context
     */
    public TopicsEntityManager(BimApp context) {
        mContext = context;
        handler = new TopicDBHandler(mContext.getContentResolver());
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
        handler = new TopicDBHandler(mContext.getContentResolver());
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
        handler.startQuery(1, controllerCallback, DataProvider.ParseUri(DataProvider.TOPIC_TABLE),
                null, mContext.getActiveProject().getProjectId(), null, null);
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
        handler.startInsert(1, null, DataProvider.ParseUri(DataProvider.TOPIC_TABLE), topic.getValues());
        NetworkConnManager.networkRequest(mContext, Request.Method.POST,
                APICall.POSTTopics(mContext.getActiveProject()), new TopicPostCallback(controllerCallback, topic), topic);

    }

    /**
     * Method to update a topic
     *
     * @param controllerCallback This is where the onSuccess/onError methods must be implemented
     * @param topic              the topic you wish to update
     */
    public void putTopic(TopicFragmentInterface controllerCallback, Topic topic) {
        topic.updatedLocally();
        if (topic.getLocalStatus() != AppDatabase.statusTypes.New)
            NetworkConnManager.networkRequest(mContext, Request.Method.PUT,
                    APICall.PUTTopic(mContext.getActiveProject(), topic), new putTopicsCallback(controllerCallback, topic.getProjectId()), topic);
    }

    /**
     * Inner class which handles the Callbacks from Volley on a postTopic request
     */
    private class TopicPostCallback implements Callback<String> {

        NewTopicFragmentInterface mTopicsFragmentInterface;
        Topic localUnGuidedVersion;

        public TopicPostCallback(NewTopicFragmentInterface callback, Topic topic) {
            mTopicsFragmentInterface = callback;
            localUnGuidedVersion = topic;
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
                handler.startDelete(1, null, DataProvider.ParseUri(DataProvider.TOPIC_TABLE),
                        localUnGuidedVersion.getMGuid(), null);
                Topic topic = new Topic(object, mContext.getActiveProject().getProjectId());
                handler.startInsert(1,null, DataProvider.ParseUri(DataProvider.TOPIC_TABLE), topic.getValues());
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
            if (response != null) Log.d("getTopicsError", response);
        }

        @Override
        public void onSuccess(String response) {
            List<Topic> topics = null;
            try {
                JSONArray jsonArray = new JSONArray(response);
                topics = EntityListConstructor.Topics(jsonArray, mContext.getActiveProject().getProjectId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (Topic t : topics) {
                handler.startInsert(1, null, DataProvider.ParseUri(DataProvider.TOPIC_TABLE), t.getValues());
            }
            mControllerCallback.setTopics(topics);
        }
    }

    /**
     * Inner class which handles the callbacks from Volley on a putTopics request
     */
    private class putTopicsCallback implements Callback<String> {

        TopicFragmentInterface mControllerCallback;
        String mProjectId;

        public putTopicsCallback(TopicFragmentInterface controllerCallback, String projectId) {
            mControllerCallback = controllerCallback;
            mProjectId = projectId;
        }

        @Override
        public void onError(String response) {
            Log.d("TopicsEntityManager", response);
        }

        @Override
        public void onSuccess(String response) {
            Log.d("PUTCOMMENT:", response);
            try {
                JSONObject object = new JSONObject(response);
                Topic Topic = new Topic(object, mProjectId);
                handler.startInsert(1, null, DataProvider.ParseUri(DataProvider.TOPIC_TABLE), Topic.getValues());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
