package com.bimapp.view.interfaces;

import android.graphics.Bitmap;

import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Topic;

import java.util.List;

public interface TopicViewInterface extends ViewMVP {
    interface TopicListener {
        void changedTopic();
        void takePicture();
        void postComment(String commentString);
        void storeCommentDraft(String commentString);
    }

    void registerListener(TopicListener listener);

    void unregisterListener();

    void setTopic(Topic topic);

    Topic getTopic();

    void setNewComment(String CommentString);

    void setComments(List<Comment> comments);

    void addComment(Comment comment);

    void gotPicture(Bitmap image);

    void deletePicture();


}
