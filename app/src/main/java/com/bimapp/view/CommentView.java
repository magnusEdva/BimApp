package com.bimapp.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bimapp.R;
import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Topic;
import com.bimapp.view.interfaces.CommentViewInterface;

public class CommentView implements CommentViewInterface {

    private View mRootView;
    private CommentViewListener mListener;

    private TextView mTitleText;
    private TextView mCommentText;
    private EditText mInputText;
    private Button mSubmitButton;
    private Button mGetPictureButton;
    public ImageView mImageView;

    public CommentView(@NonNull LayoutInflater inflater,
                       @NonNull ViewGroup container) {
        mRootView = inflater.inflate(R.layout.view_new_comment, container, false);

        mTitleText = mRootView.findViewById(R.id.comment_view_titleTextView);
        mCommentText = mRootView.findViewById(R.id.comment_viewCommentTextView);
        mInputText = mRootView.findViewById(R.id.comment_viewinputField);
        mSubmitButton = mRootView.findViewById(R.id.comment_viewpostButton);
        mGetPictureButton = mRootView.findViewById(R.id.comment_take_picture);
        mImageView = mRootView.findViewById(R.id.comment_picture);
        onPostbutton();
        onGetPictureButton();
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
        mTitleText.setText(topic.getMTitle());
        mCommentText.setText(topic.getMDescription());
    }

    @Override
    public void setComment(Comment comment) {
        mCommentText.setText(comment.getMComment());
    }

    @Override
    public void setImage(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public void clear() {
        mSubmitButton.setEnabled(true);
        mGetPictureButton.setEnabled(true);
        mImageView.setImageBitmap(null);
        mInputText.getText().clear();
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
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubmitButton.setEnabled(false);
                mGetPictureButton.setEnabled(false);
                mListener.postComment(mInputText.getText().toString());
            }
        });
    }


    private void onGetPictureButton() {
        mGetPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.takePicture();
            }
        });
    }
}
