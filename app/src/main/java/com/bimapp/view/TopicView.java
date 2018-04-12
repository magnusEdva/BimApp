package com.bimapp.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bimapp.R;
import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Topic;
import com.bimapp.view.interfaces.TopicViewInterface;

import java.util.List;

public class TopicView implements TopicViewInterface{

    private View mRootView;
    private TopicListener mListener;

    private TextView mTitleText;

    public TopicView(LayoutInflater inflater, ViewGroup container){
        mRootView = inflater.inflate(R.layout.view_topic, container, false);
        mTitleText = mRootView.findViewById(R.id.TitleText);
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
        for(Comment c : comments)
            Log.d("comment:" , c.toString());

    }

}
