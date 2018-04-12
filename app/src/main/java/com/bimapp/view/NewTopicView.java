package com.bimapp.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bimapp.R;
import com.bimapp.model.entity.Template.Template;
import com.bimapp.model.entity.Topic;
import com.bimapp.view.adapters.TemplateAdapter;
import com.bimapp.view.interfaces.NewTopicViewInterface;

import java.util.List;

/**
 * View associated with posting a new topic.
 *
 * What fields of the topic is visible/already filled out should be defined in the {@link Template} given
 * as argument in the setTemplate method, which is from the Presenter.
 */
public class NewTopicView implements NewTopicViewInterface {

    private View mRootView;
    private NewTopicToPresenter mListener;
    private RecyclerView mRecyclerView;
    private TemplateAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    // Do I need the inflater for later? Probably
    private LayoutInflater mInflater;
    private Button mSubmit;

    public NewTopicView(LayoutInflater inflater, ViewGroup container, final Template template){
        mInflater = inflater;
        mRootView = mInflater.inflate(R.layout.view_newtopic,container,false);
        // RecyclerView might be overkill for the purpose of this view. Implemented as a training exercise.
        mRecyclerView = mRootView.findViewById(R.id.newTopicRecycleView);
        mLayoutManager = new LinearLayoutManager(mRootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TemplateAdapter(template, this);
        mRecyclerView.setAdapter(mAdapter);

        // Instantiate submit button and define what it shall do when clicked.
        mSubmit = mRootView.findViewById(R.id.issue_submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNewTopic(template);
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
        // Get the fields!
        EditText status_input = mRootView.findViewById(R.id.issue_status_input);
        String status = status_input.getText().toString();

        EditText name_input = mRootView.findViewById(R.id.issue_name_input);
        String name = name_input.getText().toString();

        EditText description_input = mRootView.findViewById(R.id.issue_description_input);
        String description = description_input.getText().toString();

        // Make new topic from fields
        Topic topic = new Topic(name,null,status,null,description);

        // Tell fragment that topic has been posted
        Log.d("Posting topic", "Name of topic " + name );
        mListener.onPostTopic(topic);
    }
}


