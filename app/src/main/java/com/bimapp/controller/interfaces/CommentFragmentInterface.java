package com.bimapp.controller.interfaces;

import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Topic;

public interface CommentFragmentInterface {


    public interface commentFragmentListener{
        void postComment(CommentFragmentInterface listener, Topic topic, Comment comment);
    }

    /**
     * posted a commment and received a response.
     * @param success true if sucess or false if failure.
     * @param comment comment that was posted or null if error
     */
    void postedComment(boolean success, Comment comment);
}
