package com.bimapp.model.entityManagers;

import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.CommentFragmentInterface;
import com.bimapp.controller.interfaces.TopicFragmentInterface;
import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.EntityListConstructor;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.entity.Viewpoint;
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

    public CommentEntityManager(BimApp context) {
        mContext = context;
    }

    /**
     * acquires all comments belonging to this topic.
     *
     * @param listener to receive the appropriate response.
     * @param topic    The comment/image is to be associated with
     */
    @Override
    public void getComments(TopicFragmentInterface listener, Topic topic) {
        requestComments(new getCommentsCallback(listener), topic);
    }

    /**
     * posts a single Comment.
     *
     * @param listener to receive the appropriate response.
     * @param topic    The comment/image is to be associated with
     * @param comment  to be posted
     */
    @Override
    public void postComment(CommentFragmentInterface listener, Topic topic, Comment comment) {
        postComment(new postCommentCallback(listener), topic, comment);
    }

    /**
     * posts a single image with a comment attached.
     *
     * @param listener to receive the appropriate response.
     * @param topic    The comment/image is to be associated with
     * @param comment  to be connected to the image
     * @param file     base64 encoded string
     */
    @Override
    public void postComment(CommentFragmentInterface listener, Topic topic, Comment comment, Bitmap file) {
        Viewpoint vp = new Viewpoint(Viewpoint.SNAPSHOT_TYPE_JPG, file);
        postImage(new postViewpointCallback(listener, topic, comment), topic, vp);
    }

    private void requestViewpoint(TopicFragmentInterface listener, Comment comment) {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET,
                APICall.GETViewpoint(mContext.getActiveProject(), comment.getTopicGuid(), comment),
                new getViewpointCallback(listener, comment), null);
    }

    private void requestSnapshot(TopicFragmentInterface listener, Comment comment, Viewpoint vp) {
        NetworkConnManager.networkRequest(mContext, 11,
                APICall.GETSnapshot(mContext.getActiveProject(), comment.getTopicGuid(), vp),
                new getSnapshotCallback(listener, vp, comment), null);
    }

    private void requestComments(getCommentsCallback Callback, Topic topic) {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET,
                APICall.GETComments(mContext.getActiveProject(), topic),
                Callback, null);
    }

    private void postComment(postCommentCallback callback, Topic topic, Comment comment) {
        NetworkConnManager.networkRequest(mContext, Request.Method.POST,
                APICall.POSTComment(mContext.getActiveProject(), topic),
                callback, comment);
    }

    private void postImage(postViewpointCallback callback, Topic topic, Viewpoint vp) {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET,
                APICall.POSTViewpoints(mContext.getActiveProject(), topic),
                callback, vp);
    }


    private class getCommentsCallback implements Callback<String> {
        TopicFragmentInterface mControllerCallback;

        getCommentsCallback(TopicFragmentInterface callback) {
            mControllerCallback = callback;
        }

        @Override
        public void onSuccess(String response) {
            List<Comment> comments = null;
            try {
                JSONArray jsonArray = new JSONArray(response);
                comments = EntityListConstructor.Comments(jsonArray);


                for (Comment comment : comments) {
                    if (comment.getViewpointGuid() != null) {
                        requestViewpoint(mControllerCallback, comment);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            mControllerCallback.setComments(comments);
        }

        @Override
        public void onError(String response) {

        }
    }

    private class postCommentCallback implements Callback<String> {

        CommentFragmentInterface mListener;

        postCommentCallback(CommentFragmentInterface listener) {
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

    private class getViewpointCallback implements Callback<String> {
        TopicFragmentInterface mControllerCallback;
        Comment mComment;

        getViewpointCallback(TopicFragmentInterface callback, Comment comment) {
            mControllerCallback = callback;
            mComment = comment;
        }

        @Override
        public void onSuccess(String response) {
            Viewpoint vp = null;
            try {
                JSONObject jsonObject = new JSONObject(response);
                vp = new Viewpoint(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (vp != null && vp.hasSnapshot())
                requestSnapshot(mControllerCallback, mComment, vp);
            else {
                mComment.setViewpoint(vp);
                mControllerCallback.editComment(mComment);
            }
        }

        @Override
        public void onError(String response) {

        }
    }

    private class postViewpointCallback implements Callback<String> {

        CommentFragmentInterface mListener;
        Topic mTopic;
        Comment mComment;

        postViewpointCallback(CommentFragmentInterface listener, Topic topic, Comment comment) {
            mListener = listener;
            mTopic = topic;
            mComment = comment;
        }

        @Override
        public void onError(String response) {
            mListener.postedComment(false, null);
        }

        @Override
        public void onSuccess(String response) {
            Viewpoint vp;
            try {
                JSONObject jsonObject = new JSONObject(response);
                vp = new Viewpoint(jsonObject);
                mComment.setViewpointGuid(vp.getGuid());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            postComment(mListener, mTopic, mComment);
        }
    }

    private class getSnapshotCallback implements Callback<Bitmap> {
        TopicFragmentInterface mControllerCallback;
        Viewpoint mViewpoint;
        Comment mComment;

        getSnapshotCallback(TopicFragmentInterface callback, Viewpoint vp, Comment comment) {
            mControllerCallback = callback;
            mComment = comment;
            mViewpoint = vp;
        }

        @Override
        public void onSuccess(Bitmap response) {
            mViewpoint.constructSnapshot(response);
            mComment.setViewpoint(mViewpoint);
            mControllerCallback.editComment(mComment);

        }

        @Override
        public void onError(String response) {
            Log.d("CommentSnapshot", response);
        }
    }
}
