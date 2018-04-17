package com.bimapp.controller.interfaces;

import android.graphics.Bitmap;

import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Topic;

import java.io.File;

public interface CommentFragmentInterface {


    interface commentFragmentListener{
        void postComment(CommentFragmentInterface listener, Topic topic, Comment comment);
        void postImage(CommentFragmentInterface listener, Topic topic,Comment comment, Bitmap file);
    }

    /**
     * posted a commment and received a response.
     * @param success true if sucess or false if failure.
     * @param comment comment that was posted or null if error
     */
    void postedComment(boolean success, Comment comment);
}
