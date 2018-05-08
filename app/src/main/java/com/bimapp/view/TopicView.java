package com.bimapp.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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

import static android.content.Context.INPUT_METHOD_SERVICE;

public class TopicView implements TopicViewInterface{

    private View mRootView;
    private TopicListener mListener;

    private TextView mDueDateText;
    private ImageView mDueDateImage;

    private EditText mTitleText;
    private EditText mDescText;

    private Spinner mAssignedTo;
    private Spinner mTypeInput;
    private Spinner mStatusInput;

    private ImageView mFullScreenImage;

    private ArrayAdapter<String> mTypeAdapter;
    private ArrayAdapter<String> mStatusAdapter;
    private ArrayAdapter<String> mUserAdapter;

    private FloatingActionButton floatingButton;

    private CommentAdapter mCommentsAdapter;

    private LinearLayoutManager linearLayoutManager;

    private BimApp mContext;

    private List<String> mStatusFields;

    private List<String> mTypeFields;

    private CardView mCommentCard;
    private ImageButton mCommentAddImage;
    private EditText mNewComment;

    private List<String> mUserId;
    public TopicView(LayoutInflater inflater, ViewGroup container){
        mRootView = inflater.inflate(R.layout.view_topic, container, false);
        mContext = (BimApp) mRootView.getContext().getApplicationContext();
        mTitleText = mRootView.findViewById(R.id.TitleText);
        mTitleText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mTitleText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        mAssignedTo = mRootView.findViewById(R.id.view_topic_assigned_to);
        mTypeInput = mRootView.findViewById(R.id.view_topic_type_input);
        mStatusInput = mRootView.findViewById(R.id.view_topic_status_input);
        floatingButton = mRootView.findViewById(R.id.view_Topic_floating_button);
        mFullScreenImage = mRootView.findViewById(R.id.view_topic_comment_fullscreen_image);
        mDueDateText = mRootView.findViewById(R.id.view_topic_due_date);
        mDueDateImage = mRootView.findViewById(R.id.view_topic_due_date_image);
        mCommentCard = mRootView.findViewById(R.id.topic_comment_card);
        mDescText = mRootView.findViewById(R.id.DescriptionText);
        mCommentAddImage = mRootView.findViewById(R.id.view_topic_comment_add_image);
        mDescText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mDescText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        mNewComment = mRootView.findViewById(R.id.view_topic_comment_new);
        mNewComment.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mNewComment.setRawInputType(InputType.TYPE_CLASS_TEXT);
        RecyclerView commentsList = mRootView.findViewById(R.id.view_topic_comment_list);
        linearLayoutManager = new LinearLayoutManager(mRootView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        commentsList.setLayoutManager(linearLayoutManager);

        mCommentsAdapter = new CommentAdapter(mFullScreenImage);
        commentsList.setAdapter(mCommentsAdapter);


        setFullScreenImageOnClick();

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingButton.setVisibility(View.INVISIBLE);
                mCommentCard.setVisibility(View.VISIBLE);
                mNewComment.requestFocus();
                InputMethodManager inputMethodManager =
                        (InputMethodManager)mRootView.getContext().getSystemService(mRootView.getContext().INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInputFromWindow(
                        mNewComment.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED, 0);
            }
        });
        mCommentAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mListener.takePicture();
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
        mTitleText.setText(topic.getMTitle());
        mDescText.setText(topic.getMDescription());
        mTypeInput.setAdapter(mTypeAdapter);
        mStatusInput.setAdapter(mStatusAdapter);

        if(topic.getMDueDate() != null){
            mDueDateText.setText(topic.getMDueDate());
            mDueDateImage.setVisibility(View.VISIBLE);
        } else {
            mDueDateText.setText("");
            mDueDateImage.setVisibility(View.GONE);
        }
        mTypeFields = mContext.getActiveProject().getProjectTypesOrdered(topic);
        mTypeAdapter =  new ArrayAdapter<String>(mRootView.getContext()
                , R.layout.support_simple_spinner_dropdown_item
                , mTypeFields);

        mStatusFields = mContext.getActiveProject().getProjectStatusOrdered(topic);
        mStatusAdapter =  new ArrayAdapter<String>(mRootView.getContext()
                , R.layout.support_simple_spinner_dropdown_item
                , mStatusFields);
        mUserId = mContext.getActiveProject().getProjectUsersOrdered(topic);
        mUserAdapter = new ArrayAdapter<String>(mRootView.getContext()
                , R.layout.support_simple_spinner_dropdown_item
                , mUserId);
        mAssignedTo.setAdapter(mUserAdapter);
        mTypeInput.setAdapter(mTypeAdapter);
        mStatusInput.setAdapter(mStatusAdapter);


        mTypeInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!topic.getMTopicType().equals(mTypeFields.get(position))) {
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
                if(!topic.getMTopicStatus().equals(mStatusFields.get(position))) {
                    topic.setTopicStatus(mStatusFields.get(position));
                    mListener.changedTopic();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        mAssignedTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!topic.getMAssignedTo().equals(mUserId.get(position))) {
                    topic.setAssignedTo(mUserId.get(position));
                    mListener.changedTopic();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        mTitleText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    topic.setTitle(mTitleText.getText().toString());
                    mListener.changedTopic();
                    mRootView.clearFocus();
                    clearKeyboard();
                    return true;
                }
                return false;
            }
        });
        mDescText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    topic.setDescription(mDescText.getText().toString());
                    mDescText.clearFocus();
                    clearKeyboard();
                    mListener.changedTopic();
                    return true;
                }
                return false;
            }
        });
        mNewComment.getText().clear();
        mNewComment.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mListener.postComment(mNewComment.getText().toString());
                    mCommentsAdapter.addComment(new Comment(mNewComment.getText().toString()));
                    mNewComment.getText().clear();
                    mDescText.clearFocus();
                    clearKeyboard();
                    mCommentAddImage.setImageDrawable(mRootView.getContext().getDrawable(R.drawable.ic_topics_got_image));
                    mCommentCard.setVisibility(View.GONE);
                    floatingButton.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
    }


    private void clearKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) mRootView.getContext().
                getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mRootView.getWindowToken(), 0);
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

    @Override
    public void gotPicture(){
        mCommentAddImage.setImageDrawable(mRootView.getContext().getDrawable(R.drawable.ic_topics_got_image));
    }

}
