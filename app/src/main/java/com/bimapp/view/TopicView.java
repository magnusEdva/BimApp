package com.bimapp.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bimapp.R;
import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Topic;
import com.bimapp.view.adapters.CommentAdapter;
import com.bimapp.view.interfaces.TopicViewInterface;

import java.util.List;

public class TopicView implements TopicViewInterface{

    private View mRootView;
    private TopicListener mListener;

    private TextView mTitleText;
    private TextView mAssignedToText;
    private TextView mRequestedByText;
    private Button mTypeButton;
    private Button mStatusButton;
    private FloatingActionButton floatingButton;

    private CommentAdapter mCommentsAdapter;

    private LinearLayoutManager linearLayoutManager;

    public TopicView(LayoutInflater inflater, ViewGroup container){
        mRootView = inflater.inflate(R.layout.view_topic, container, false);
        mTitleText = mRootView.findViewById(R.id.TitleText);
        mAssignedToText = mRootView.findViewById(R.id.view_topic_assigned_to);
        mRequestedByText = mRootView.findViewById(R.id.view_topic_requested_by);
        mTypeButton = mRootView.findViewById(R.id.view_topic_TypeButton);
        mStatusButton = mRootView.findViewById(R.id.view_topic_StatusButton);
        floatingButton = mRootView.findViewById(R.id.view_Topic_floating_button);

        RecyclerView commentsList = mRootView.findViewById(R.id.view_topic_comment_list);
        linearLayoutManager = new LinearLayoutManager(mRootView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentsList.setLayoutManager(linearLayoutManager);

        mCommentsAdapter = new CommentAdapter();
        commentsList.setAdapter(mCommentsAdapter);

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.newComment();
            }
        });
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    @Override
    public void registerListener(TopicListener listener) {
        mListener = listener;
    }

    @Override
    public void unregisterListener() {
        mListener = null;
    }

    @Override
    public void setTopic(Topic topic) {
        mTitleText.setText(topic.getmTitle());
        mRequestedByText.setText(topic.getCreationAuthor());
        mAssignedToText.setText(topic.getAssignedTo());
        mTypeButton.setText(topic.getTopicType());
        mStatusButton.setText(topic.getTopicStatus());
    }


    @Override
    public void setComments(List<Comment> comments){
        mCommentsAdapter.setComments(comments);
    }


}
