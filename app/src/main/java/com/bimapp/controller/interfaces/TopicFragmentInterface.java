package com.bimapp.controller.interfaces;

import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Topic;

import java.util.List;

public interface TopicFragmentInterface {
    interface CommentFragmentListener{
       void getComments(TopicFragmentInterface presenterListener, Topic topic);
    }

    void setComments(List<Comment> comments);
}