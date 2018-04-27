package com.bimapp.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.bimapp.BimApp;
import com.bimapp.R;
import com.bimapp.controller.interfaces.CommentFragmentInterface;
import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Template.Template;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.entity.Viewpoint;
import com.bimapp.model.DataAccess.entityManagers.CommentEntityManager;
import com.bimapp.view.adapters.TemplateAdapter;
import com.bimapp.view.interfaces.NewTopicViewInterface;

import static android.content.Context.INPUT_METHOD_SERVICE;

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

    private Button mSubmit;
    private Bitmap mImage;

    private String mCommentString;

    public NewTopicView(LayoutInflater inflater, ViewGroup container, final Template template){

        mRootView = inflater.inflate(R.layout.view_newtopic,container,false);
        // RecyclerView might be overkill for the purpose of this view. Implemented as a training exercise.
        mRecyclerView = mRootView.findViewById(R.id.newTopicRecycleView);
        mLayoutManager = new LinearLayoutManager(mRootView.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TemplateAdapter(template, this); // Shouldn't need these params
        mRecyclerView.setAdapter(mAdapter);

        // Instantiate submit button and define what it shall do when clicked.
        mSubmit = mRootView.findViewById(R.id.issue_submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Make sure that all required fields are filled in

                // Hides the keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) mRootView.getContext().
                        getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mRootView.getWindowToken(), 0);

                makeNewTopic();
            }
        });

        Log.d("New View", "Made new view");
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
    public void setImage(Bitmap image) {

        mImage = image;

    }

    @Override
    public void postedTopic(final Topic topic) {
        Viewpoint vp = null;
        Comment comment = new Comment(mCommentString);
        if(mImage != null){
            vp = new Viewpoint(Viewpoint.SNAPSHOT_TYPE_JPG, mImage);
            comment.setViewpoint(vp);
            //comment.setViewpointGuid(topic.getGuid());
        }
        BimApp context = (BimApp) mRootView.getContext().getApplicationContext();

        if(mCommentString != "" && mImage != null){
        CommentEntityManager cm = new CommentEntityManager(context);
        cm.postComment(new CommentFragmentInterface() {
            @Override
            public void postedComment(boolean success, Comment comment) {

            }
        }, topic, comment, mImage);
        } else if (mCommentString != "" && mImage == null){
            CommentEntityManager cm = new CommentEntityManager(context);
            cm.postComment(new CommentFragmentInterface() {
                @Override
                public void postedComment(boolean success, Comment comment) {

                }
            }, topic, comment);
        }


    }


    /**
     * Method to construct a topic from the user input.
     * contains a callback to the presenter, tells it that a topic has been made.
     *
     * // TODO Implement a return boolean to validate?
     */
    public void makeNewTopic() {
        // Get the fields!
        Spinner status_input = mRootView.findViewById(R.id.topic_status_input);
        String topic_status = status_input.getSelectedItem().toString();

        EditText title_input = mRootView.findViewById(R.id.topic_title_input);
        String title = title_input.getText().toString();

        Spinner assignedTo_input = mRootView.findViewById(R.id.topic_assigned_to_input);
        String assignedTo = assignedTo_input.getSelectedItem().toString();

        EditText description_input = mRootView.findViewById(R.id.topic_description_input);
        String description = description_input.getText().toString();

        Spinner topicType_input = mRootView.findViewById(R.id.topic_type_input);
        String topicType = topicType_input.getSelectedItem().toString();

        // Make new topic from fields
        Topic topic = new Topic(title,topicType,topic_status,assignedTo,description);

        EditText comment_input = mRootView.findViewById(R.id.topic_comment_input);
        mCommentString = comment_input.getText().toString();
        // Tell fragment that topic has been posted
        Log.d("Posting topic", "Name of topic " + title );
        mListener.onPostTopic(topic);
    }

    /**
     * Override from the interface {@link NewTopicViewInterface} which extends
     * {@link android.view.View.OnClickListener}.
     * Passes information to the fragment {@link com.bimapp.controller.FragmentNewTopic}
     *
     * @param view the view in which the item was clicked
     */
    @Override
    public void onClick(View view) {
        mListener.onCameraIntent(view);
    }

    @Override
    public void updateExtensionsDefaultValue(){
        mAdapter.setExtensionsDefaultValue();
    }
}


