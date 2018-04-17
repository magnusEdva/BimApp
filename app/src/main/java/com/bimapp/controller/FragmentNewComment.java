package com.bimapp.controller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.CommentFragmentInterface;
import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.entityManagers.CommentEntityManager;
import com.bimapp.model.entityManagers.ProjectEntityManager;
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
        mCommentView = new CommentView(inflater, container);
        mCommentView.attachListener(this);
        mCommentView.setTopic(mTopic);
        return mCommentView.getRootView();
    }

    @Override
    public void postComment(String commentContent) {
        Comment comment = new Comment(commentContent);
        commentEntityManager.postComment(this, mTopic, comment);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCommentView.detachListener();
    }

    @Override
    public void editComment(String commentContent) {

    }

    public static void setTopic(Topic topic){mTopic = topic; }

    @Override
    public void postedComment(boolean success, Comment comment) {
        if(comment .getComment() != null)
            Log.d("NewComment",success + " " + comment.getComment());
    }

}
