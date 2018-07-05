package com.bimapp.controller;


import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.CommentFragmentInterface;
import com.bimapp.controller.interfaces.TopicFragmentInterface;
import com.bimapp.model.data_access.entityManagers.CommentEntityManager;
import com.bimapp.model.data_access.entityManagers.TopicsEntityManager;
import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Topic;
import com.bimapp.view.TopicView;
import com.bimapp.view.interfaces.TopicViewInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTopic extends Fragment implements CommentFragmentInterface, TopicFragmentInterface, TopicViewInterface.TopicListener {

    private static Topic mTopic;
    private TopicViewInterface mTopicView;
    private CommentEntityManager commentManager;
    private TopicsEntityManager mTopicManager;
    private BimApp mContext;
    private TopicFragmentListener mListener;
    private List<Comment> mComments;
    private Bitmap mImage;
    private String commentString;

    public FragmentTopic() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (BimApp) this.getActivity().getApplication();
        commentManager = new CommentEntityManager(mContext);
        mTopicManager = new TopicsEntityManager(mContext);
        mComments = new ArrayList<>();
        commentString = "";

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TopicFragmentListener)
            mListener = (TopicFragmentListener) context;
        else
            throw new UnsupportedOperationException();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTopicView = new TopicView(inflater, container);
        mTopicView.registerListener(this);
        commentManager.getComments(this, mTopic);
        return mTopicView.getRootView();
    }

    @Override
    public void onResume() {
        super.onResume();
        commentManager.getComments(this, mTopic);
            mTopicView.setTopic(mTopic);
            mTopicView.setNewComment(commentString);
    }

    public void setTopic(Topic topic) {
        mTopic = topic;
        commentString = "";
        if(mTopicView != null)
            mTopicView.deletePicture();
    }

    @Override
    public void setComments(List<Comment> comments) {
        mTopicView.setComments(comments);
        mComments.clear();
        mComments.addAll(comments);
    }

    @Override
    public void addComment(Comment comment) {
        if(mComments.contains(comment)) {
            int i = mComments.indexOf(comment);
            mComments.set(i, comment);
        }else
            mComments.add(comment);
        mTopicView.setComments(mComments);
    }

    @Override
    public void editComment(Comment comment) {
        if(mComments.contains(comment)) {
            int i = mComments.indexOf(comment);
            mComments.set(i, comment);
            mTopicView.setComments(mComments);
        }

    }


    @Override
    public void changedTopic() {
        mTopicManager.putTopic(this, mTopic);
    }

    @Override
    public void takePicture() {
        mListener.onTakePhoto();
    }

    public void setImage(Bitmap image) {
        this.mImage = image;
        mTopicView.gotPicture(image);
    }

    @Override
    public void postedComment(boolean success, Comment comment) {
        Log.d("comment", "comment");
    }

    public interface TopicFragmentListener {
        void onTakePhoto();
    }

    @Override
    public void postComment(String commentContent) {
        Comment comment = new Comment(commentContent);
        if (mImage == null)
            commentManager.postComment(this, mTopic, comment);
        else
            commentManager.postComment(this, mTopic, comment, mImage);

        mImage = null;
    }

    @Override
    public void storeCommentDraft(String commentString){
        this.commentString = commentString;
    }
}
