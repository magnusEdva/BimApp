package com.bimapp.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bimapp.R;
import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Topic;
import com.bimapp.view.interfaces.CommentViewInterface;

public class CommentView implements CommentViewInterface {

    private View mRootView;
    private CommentViewListener mListener;

    private TextView titleText;
    private TextView commentText;
    private EditText inputText;
    private Button button;

    public CommentView(@NonNull LayoutInflater inflater,
                       @NonNull ViewGroup container) {
        mRootView = inflater.inflate(R.layout.view_new_comment, container, false);

        titleText = mRootView.findViewById(R.id.comment_view_titleTextView);
        commentText = mRootView.findViewById(R.id.comment_viewCommentTextView);
        inputText = mRootView.findViewById(R.id.comment_viewinputField);
        button = mRootView.findViewById(R.id.comment_viewpostButton);
        onPostbutton();
    }

    @Override
    public void attachListener(CommentViewListener listener) {
        mListener = listener;
    }

    @Override
    public void detachListener() {
        mListener = null;
    }

    @Override
    public void setTopic(Topic topic) {
        titleText.setText(topic.getmTitle());
        commentText.setText(topic.getDescription());
    }

    @Override
    public void setComment(Comment comment) {
        commentText.setText(comment.getComment());
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    public void onPostbutton(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.postComment(inputText.getText().toString());
            }
        });
    }
}
