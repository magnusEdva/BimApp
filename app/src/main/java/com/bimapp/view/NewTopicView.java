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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bimapp.R;
import com.bimapp.model.entity.Template.Template;
import com.bimapp.model.entity.Topic;
import com.bimapp.view.adapters.TemplateAdapter;
import com.bimapp.view.interfaces.NewTopicInterface;

/**
 * View associated with posting a new topic.
 *
 * What fields of the topic is visible/already filled out should be defined in the {@link Template} given
 * as argument in the makeNewTopic method, which is from the Presenter.
 */
public class NewTopicView implements NewTopicInterface {

    private View mRootView;
    private NewTopicToPresenter mListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    // Do I need the inflater for later? Probably
    private LayoutInflater mInflater;

    public NewTopicView(LayoutInflater inflater, ViewGroup container){
        mInflater = inflater;
        mRootView = mInflater.inflate(R.layout.view_newtopic,container,false);
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

    /**
     * Method started from the Presenter of this view.
     * @param template
     */
    @Override
    public void makeNewTopic(Template template) {
        // RecyclerView is really overkill for the purpose of this view. Implemented as a training exercise.
        mRecyclerView = mRootView.findViewById(R.id.newTopicRecycleView);
        mLayoutManager = new LinearLayoutManager(mRootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TemplateAdapter(template);
        mRecyclerView.setAdapter(mAdapter);

        LinearLayout ll = new LinearLayout(mRootView.getContext(),null);
        // ll.addView(mRootView);

        //TextView
        Topic topic = new Topic(template.getTitle(),null,null,null,null);
        //template.getTitle();

        mListener.onPostTopic(topic);
    }
}


