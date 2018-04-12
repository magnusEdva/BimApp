package com.bimapp.view.interfaces;

import com.bimapp.model.entity.Topic;

public interface TopicViewInterface extends ViewMVP {
    interface TopicListener {

    }

    void registerListener(TopicListener listener);

    void unregisterListener();

    void setTopic(Topic topic);
}
