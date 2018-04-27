package com.bimapp.model.data_access.entityManagers;

import android.content.AsyncQueryHandler;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.CommentFragmentInterface;
import com.bimapp.controller.interfaces.TopicFragmentInterface;
import com.bimapp.model.data_access.AppDatabase;
import com.bimapp.model.data_access.DataProvider;
import com.bimapp.model.data_access.network.APICall;
import com.bimapp.model.data_access.network.Callback;
import com.bimapp.model.data_access.network.NetworkConnManager;
import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.EntityListConstructor;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.entity.Viewpoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CommentEntityManager implements TopicFragmentInterface.topicFragmentListener,
        CommentFragmentInterface.commentFragmentListener {

    private BimApp mContext;

    private ContentResolver contentResolver;

    private static CommentDBHandler handler;


    public CommentEntityManager(BimApp context) {
        mContext = context;
        contentResolver = context.getContentResolver();
        handler = new CommentDBHandler(contentResolver);
    }

    /**
     * acquires all comments belonging to this topic.
     *
     * @param listener to receive the appropriate response.
     * @param topic    The comment/image is to be associated with
     */
    @Override
    public void getComments(TopicFragmentInterface listener, Topic topic) {
        handler.startQuery(1, listener, ParseUri(), null, topic.getMGuid(), null, null);
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
        new postImage(new postViewpointCallback(listener, topic, comment), topic, vp).execute();
    }

    private void requestViewpoint(TopicFragmentInterface listener, Comment comment) {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET,
                APICall.GETViewpoint(mContext.getActiveProject(), comment.getMTopicGuid(), comment),
                new getViewpointCallback(listener, comment), null);
    }

    private void requestSnapshot(TopicFragmentInterface listener, Comment comment, Viewpoint vp) {
        NetworkConnManager.networkRequest(mContext, 11,
                APICall.GETSnapshot(mContext.getActiveProject(), comment.getMTopicGuid(), vp),
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

    private class postImage extends AsyncTask<Void, Integer, Boolean> {
        postViewpointCallback mCallback;
        Topic mTopic;
        Viewpoint mVp;

        postImage(postViewpointCallback callback, Topic topic, Viewpoint vp) {
            super();
            mCallback = callback;
            mTopic = topic;
            mVp = vp;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            NetworkConnManager.networkRequest(mContext, Request.Method.POST,
                    APICall.POSTViewpoints(mContext.getActiveProject(), mTopic),
                    mCallback, mVp);
            return null;
        }
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
                    if (comment.getMViewpointGuid() != null) {
                        requestViewpoint(mControllerCallback, comment);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mControllerCallback.setComments(comments);
            for(Comment c : comments){
                handler.startInsert(-1, null, ParseUri(), c.getValues());
            }
        }

        @Override
        public void onError(String response) {
            if (response != null)
                Log.d("GetComments", response);
        }
    }

    private class postCommentCallback implements Callback<String> {

        CommentFragmentInterface mListener;

        postCommentCallback(CommentFragmentInterface listener) {
            mListener = listener;
        }

        @Override
        public void onError(String response) {
            if (response != null)
                Log.d("postComment", response);
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
            if (response != null) Log.d("CommentSnapshot", response);
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
            if (response != null)
                Log.d("postViewpoint", response);
        }

        @Override
        public void onSuccess(String response) {
            Viewpoint vp;
            try {
                JSONObject jsonObject = new JSONObject(response);
                vp = new Viewpoint(jsonObject);
                mComment.setViewpointGuid(vp.getMGuid());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            postComment(mListener, mTopic, mComment);
        }

    }

    private void insertCommentsIntoProvider(List<Comment> comments) {
        ContentProviderClient client = contentResolver.acquireContentProviderClient(ParseUri());
        ContentValues values = new ContentValues();

        if (client != null)
            client.release();

    }

    private static Uri ParseUri() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content");
        builder.authority(DataProvider.AUTHORITY);
        builder.path(DataProvider.CommentTable);
        return builder.build();
    }


}


