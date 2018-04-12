package com.bimapp.view.interfaces;

import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Topic;

import java.util.List;

public interface TopicViewInterface extends ViewMVP {
    interface TopicListener {

    }

    void registerListener(TopicListener listener);

    void unregisterListener();

    void setTopic(Topic topic);

    void setComments(List<Comment> comments);
}
