package com.bimapp.model.entityManagers;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.TopicFragmentInterface;
import com.bimapp.controller.interfaces.TopicsFragmentInterface;
import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.EntityListConstructor;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.network.APICall;
import com.bimapp.model.network.Callback;
import com.bimapp.model.network.NetworkConnManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class CommentEntityManager implements TopicFragmentInterface.CommentFragmentListener{

    private BimApp mContext;

    public CommentEntityManager(BimApp context){
        mContext = context;
    }

    @Override
    public void getComments(TopicFragmentInterface listener, Topic topic) {
        requestComments(new getCommentsCallback(listener), topic);
    }

    private class getCommentsCallback implements Callback{
        TopicFragmentInterface mControllerCallback;

        public getCommentsCallback(TopicFragmentInterface callback){
            mControllerCallback = callback;
        }
        @Override
        public void onSuccess(String response) {
            List<Comment> comments = null;
            try {
                JSONArray jsonArray = new JSONArray(response);
                comments = EntityListConstructor.Comments(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mControllerCallback.setComments(comments);
        }
        @Override
        public void onError(String response) {

        }
    }

    public void requestComments(getCommentsCallback Callback, Topic topic) {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET,
                APICall.GETComments(mContext.getActiveProject(), topic),
                Callback, null );
    }

}
