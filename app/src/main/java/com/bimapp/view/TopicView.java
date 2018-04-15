package com.bimapp.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private CommentAdapter mCommentsAdapter;

    private LinearLayoutManager linearLayoutManager;

    public TopicView(LayoutInflater inflater, ViewGroup container){
        mRootView = inflater.inflate(R.layout.view_topic, container, false);
        mTitleText = mRootView.findViewById(R.id.TitleText);
        RecyclerView commentsList = mRootView.findViewById(R.id.view_topic_comment_list);

        linearLayoutManager = new LinearLayoutManager(mRootView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentsList.setLayoutManager(linearLayoutManager);

        mCommentsAdapter = new CommentAdapter();
        commentsList.setAdapter(mCommentsAdapter);
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
    }

    @Override
    public void setComments(List<Comment> comments){
        mCommentsAdapter.setComments(comments);
    }

}
