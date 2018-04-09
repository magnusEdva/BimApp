package com.bimapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bimapp.R;
import com.bimapp.model.entity.Template.Template;
import com.bimapp.model.entity.Topic;
import com.bimapp.view.adapters.TemplateAdapter;
import com.bimapp.view.interfaces.NewTopicInterface;

/**
 * TODO: document your custom view class.
 */
public class NewTopicView implements NewTopicInterface {

    private View mRootView;
    private NewTopicToPresenter mListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public NewTopicView(LayoutInflater inflater, ViewGroup container){
        mRootView = inflater.inflate(R.layout.view_newtopic,container,false);
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
    public void registerListener(NewTopicToPresenter newTopicToPresenter) {
        mListener = newTopicToPresenter;
    }

    @Override
    public void unregisterListener() {
        mListener = null;
    }

    @Override
    public void makeNewTopic(Template template) {
        mRecyclerView = mRootView.findViewById(R.id.newTopicRecycleView);
        mLayoutManager = new LinearLayoutManager(mRootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TemplateAdapter(template);
        mRecyclerView.setAdapter(mAdapter);


        //TextView
        Topic topic = new Topic(template.getTitle(),null,null,null,null);
        //template.getTitle();

        mListener.onPostTopic(topic);
    }
}


