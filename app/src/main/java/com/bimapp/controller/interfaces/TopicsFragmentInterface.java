package com.bimapp.controller.interfaces;

import com.bimapp.model.entity.Topic;

import java.util.List;

/**
 * Created by Hakon on 22.03.2018.
 */

public interface TopicsFragmentInterface {

    void setSearchResult(List<Topic> topic);

    interface FragmentTopicsListener {
        void getTopics(TopicsFragmentInterface callback);
    }

    void setTopics(List<Topic> topics);
}
