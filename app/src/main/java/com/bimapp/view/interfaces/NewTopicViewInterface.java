package com.bimapp.view.interfaces;

import com.bimapp.model.entity.Template.Template;
import com.bimapp.model.entity.Topic;

public interface NewTopicViewInterface extends ViewMVP {

    interface NewTopicToPresenter{
        void onPostTopic(Topic topic);
    }

    void registerListener(NewTopicToPresenter newTopicToPresenter);
    void unregisterListener();
    void makeNewTopic(Template template);
}
