package com.bimapp.view.interfaces;

import android.graphics.Bitmap;

import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Topic;

public interface CommentViewInterface extends ViewMVP {

    interface CommentViewListener{
        void postComment(String comment);
        void takePicture();
        void editComment(String comment);
        void deletePicture(int index);
    }

    void attachListener(CommentViewListener listener);
    void detachListener();
    void setTopic(Topic topic);
    void setComment(Comment comment);
    void setImage(Bitmap bitmap);
    void clear();


}
