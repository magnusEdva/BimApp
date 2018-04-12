package com.bimapp.controller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.BimApp;
import com.bimapp.R;
import com.bimapp.controller.interfaces.TopicFragmentInterface;
import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.entityManagers.CommentEntityManager;
import com.bimapp.view.TopicView;
import com.bimapp.view.interfaces.TopicViewInterface;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTopic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTopic extends Fragment implements TopicFragmentInterface{

    private static Topic mTopic;
    private TopicViewInterface mTopicView;
    private CommentEntityManager commentManager;
    private BimApp mContext;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTopicView = new TopicView(inflater, container);
        mTopicView.setTopic(mTopic);
        commentManager.getComments(this, mTopic);
        return mTopicView.getRootView();
    }

    public static void setTopic(Topic topic){mTopic = topic;}

    @Override
    public void setComments(List<Comment> comments) {
        mTopicView.setComments(comments);
    }
}
