package com.bimapp.controller;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.BimApp;
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
 * Use the {@link FragmentTopic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTopic extends Fragment implements TopicFragmentInterface, TopicViewInterface.TopicListener{

    private static Topic mTopic;
    private TopicViewInterface mTopicView;
    private CommentEntityManager commentManager;
    private TopicsEntityManager mTopicManager;
    private BimApp mContext;
    private TopicFragmentListener mListener;
    private List<Comment> mComments;

    public FragmentTopic() {

    }

    public static FragmentTopic newInstance(Topic topic) {
        FragmentTopic fragment = new FragmentTopic();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        FragmentTopic.mTopic = topic;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (BimApp) this.getActivity().getApplication();
        commentManager = new CommentEntityManager(mContext);
        mTopicManager = new TopicsEntityManager(mContext);
        mComments = new ArrayList<>();
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
        mTopicView.setTopic(mTopic);
        commentManager.getComments(this, mTopic);
        return mTopicView.getRootView();
    }

    public static void setTopic(Topic topic){mTopic = topic;}

    @Override
    public void setComments(List<Comment> comments) {
        mTopicView.setComments(comments);
        mComments.clear();
        mComments.addAll(comments);
    }

    @Override
    public void editComment(Comment comment) {
        boolean found = false;
        for(int i = 0; i < mComments.size() && !found; i++){
            if(mComments.get(i).equals(comment)){
                mComments.remove(i);
                mComments.add(i,comment);
                found = true;
            }
        }
        mTopicView.setComments(mComments);
            }

    @Override
    public void newComment() {
        mListener.openCommentFragment(mTopic);
    }

    @Override
    public void changedTopic() {
        mTopicManager.putTopic(this, mTopic);
    }


    public interface TopicFragmentListener{
        void openCommentFragment(Topic topic);
    }
}
