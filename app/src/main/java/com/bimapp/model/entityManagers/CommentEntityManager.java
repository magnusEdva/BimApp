package com.bimapp.model.entityManagers;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.CommentFragmentInterface;
import com.bimapp.controller.interfaces.TopicFragmentInterface;
import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.EntityListConstructor;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.network.APICall;
import com.bimapp.model.network.Callback;
import com.bimapp.model.network.NetworkConnManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CommentEntityManager implements TopicFragmentInterface.topicFragmentListener,
        CommentFragmentInterface.commentFragmentListener {

    private BimApp mContext;

    public CommentEntityManager(BimApp context){
        mContext = context;
    }

    @Override
    public void getComments(TopicFragmentInterface listener, Topic topic) {
        requestComments(new getCommentsCallback(listener), topic);
    }

    @Override
    public void postComment(CommentFragmentInterface listener,Topic topic, Comment comment) {
        postComment(new postCommentCallback(listener), topic, comment);
    }


    private void requestComments(getCommentsCallback Callback, Topic topic) {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET,
                APICall.GETComments(mContext.getActiveProject(), topic),
                Callback, null );
    }

    private void postComment(postCommentCallback callback, Topic topic,Comment comment){
        NetworkConnManager.networkRequest(mContext, Request.Method.POST,
                APICall.POSTComment(mContext.getActiveProject(), topic),
                callback, comment);
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

    private class postCommentCallback implements  Callback{

        CommentFragmentInterface mListener;

        public postCommentCallback(CommentFragmentInterface listener){
            mListener = listener;
        }

        @Override
        public void onError(String response) {
            mListener.postedComment(false, null);
        }

        @Override
        public void onSuccess(String response) {
            Comment comment = null;
            try {
                JSONObject jsonObject = new JSONObject(response);
                comment = new Comment(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mListener.postedComment(true, comment);
        }
    }

}
