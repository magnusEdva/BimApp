package com.bimapp.controller.interfaces;

import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Topic;

import java.util.List;

public interface TopicFragmentInterface {
    interface topicFragmentListener {
       void getComments(TopicFragmentInterface presenterListener, Topic topic);
    }

    void setComments(List<Comment> comments);
    void editComment(Comment comment);
}
