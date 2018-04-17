package com.bimapp.view.interfaces;

import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Topic;

public interface CommentViewInterface extends ViewMVP {

    public interface CommentViewListener{
        void postComment(String comment);
        void editComment(String comment);
    }

    void attachListener(CommentViewListener listener);
    void detachListener();
    void setTopic(Topic topic);
    void setComment(Comment comment);


}
