package com.bimapp.controller;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.CommentFragmentInterface;
import com.bimapp.model.data_access.entityManagers.CommentEntityManager;
import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Topic;
import com.bimapp.view.CommentView;
import com.bimapp.view.interfaces.CommentViewInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNewComment extends Fragment implements CommentViewInterface.CommentViewListener, CommentFragmentInterface{

    private BimApp mContext;
    private static Topic mTopic;
    private CommentEntityManager commentEntityManager;
    private CommentViewInterface mCommentView;
    private FragmentNewTopic.OnFragmentInteractionListener mListener;
    private Bitmap mImage;

    public FragmentNewComment() {}

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mContext = (BimApp) this.getActivity().getApplication();
        commentEntityManager = new CommentEntityManager(mContext);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mCommentView == null) {
            mCommentView = new CommentView(inflater, container);
        }
        mCommentView.attachListener(this);
        mCommentView.   setTopic(mTopic);
        return mCommentView.getRootView();
    }

    @Override
    public void postComment(String commentContent) {
        Comment comment = new Comment(commentContent);
        if(mImage == null)
            commentEntityManager.postComment(this, mTopic, comment);
        else
            commentEntityManager.postComment(this, mTopic, comment, mImage);
    }

    @Override
    public void takePicture() {
        mListener.onTakePhoto();
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCommentView.detachListener();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNewTopic.OnFragmentInteractionListener) {
            mListener = (FragmentNewTopic.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void editComment(String commentContent) {

    }

    @Override
    public void deletePicture(int index) {
        mImage = null;
    }

    public static void setTopic(Topic topic){
        mTopic = topic;
    }

    @Override
    public void postedComment(boolean success, Comment comment) {
        if(comment != null)
            Log.d("NewComment",success + " " + comment.getMComment());
        mCommentView.clear();
        mImage = null;
        mListener.onFragmentFinish();
    }

    public void setImage(Bitmap image) {
        mImage = image;
        mCommentView.setImage(mImage);
    }
}
