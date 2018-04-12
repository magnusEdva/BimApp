package com.bimapp.controller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.R;
import com.bimapp.model.entity.Topic;
import com.bimapp.view.TopicView;
import com.bimapp.view.interfaces.TopicViewInterface;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTopic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTopic extends Fragment {

    private static Topic mTopic;
    private TopicViewInterface mTopicView;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTopicView = new TopicView(inflater, container);
        mTopicView.setTopic(mTopic);
        return mTopicView.getRootView();
    }

    public static void setTopic(Topic topic){mTopic = topic;}

}
