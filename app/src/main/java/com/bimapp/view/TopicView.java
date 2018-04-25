package com.bimapp.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bimapp.BimApp;
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

    private TextView mStatusText;
    private TextView mTypeText;
    private Spinner mTypeInput;
    private Spinner mStatusInput;
    private ImageView mFullScreenImage;

    private ArrayAdapter<String> mTypeAdapter;

    private ArrayAdapter<String> mStatusAdapter;

    private FloatingActionButton floatingButton;

    private CommentAdapter mCommentsAdapter;

    private LinearLayoutManager linearLayoutManager;

    private BimApp mContext;

    private List<String> mStatusFields;

    private List<String> mTypeFields;

    public TopicView(LayoutInflater inflater, ViewGroup container){
        mRootView = inflater.inflate(R.layout.view_topic, container, false);
        mContext = (BimApp) mRootView.getContext().getApplicationContext();
        mTitleText = mRootView.findViewById(R.id.TitleText);
        mAssignedToText = mRootView.findViewById(R.id.view_topic_assigned_to);
        mRequestedByText = mRootView.findViewById(R.id.view_topic_requested_by);
        mTypeInput = mRootView.findViewById(R.id.view_topic_type_input);
        mStatusInput = mRootView.findViewById(R.id.view_topic_status_input);
        floatingButton = mRootView.findViewById(R.id.view_Topic_floating_button);
        mFullScreenImage = mRootView.findViewById(R.id.view_topic_comment_fullscreen_image);

        RecyclerView commentsList = mRootView.findViewById(R.id.view_topic_comment_list);
        linearLayoutManager = new LinearLayoutManager(mRootView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentsList.setLayoutManager(linearLayoutManager);

        mCommentsAdapter = new CommentAdapter(mFullScreenImage);
        commentsList.setAdapter(mCommentsAdapter);

        mTypeText = mRootView.findViewById(R.id.view_topic_type);
        mStatusText = mRootView.findViewById(R.id.view_topic_status);

        setFullScreenImageOnClick();
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
    public void setTopic(final Topic topic) {
        mTitleText.setText(topic.getmTitle());
        mRequestedByText.setText(topic.getCreationAuthor());
        mAssignedToText.setText(topic.getAssignedTo());
        mTypeInput.setAdapter(mTypeAdapter);
        mStatusInput.setAdapter(mStatusAdapter);
        mStatusText.setText(R.string.issue_status);
        mTypeText.setText(R.string.topic_type);

        mTypeFields = mContext.getActiveProject().getProjectTypesOrdered(topic);
        mTypeAdapter =  new ArrayAdapter<String>(mRootView.getContext()
                , R.layout.support_simple_spinner_dropdown_item
                , mTypeFields);

        mStatusFields = mContext.getActiveProject().getProjectStatusOrdered(topic);
        mStatusAdapter =  new ArrayAdapter<String>(mRootView.getContext()
                , R.layout.support_simple_spinner_dropdown_item
                , mStatusFields);

        mTypeInput.setAdapter(mTypeAdapter);
        mStatusInput.setAdapter(mStatusAdapter);

        mTypeInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!topic.getTopicType().equals(mTypeFields.get(position))) {
                    topic.setTopicType(mTypeFields.get(position));
                    mListener.changedTopic();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        mStatusInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!topic.getTopicStatus().equals(mStatusFields.get(position))) {
                    topic.setTopicStatus(mStatusFields.get(position));
                    mListener.changedTopic();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }


    @Override
    public void setComments(List<Comment> comments){
        mCommentsAdapter.setComments(comments);
    }

    private void setFullScreenImageOnClick(){
        mFullScreenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFullScreenImage.setVisibility(View.GONE);
            }
        });
    }

}
