package com.bimapp.view.interfaces;

import android.view.View;

import com.bimapp.model.entity.Template.Template;
import com.bimapp.model.entity.Topic;

public interface NewTopicViewInterface extends ViewMVP, View.OnClickListener {


    interface NewTopicToPresenter{
        void onPostTopic(Topic topic);
        void onCameraIntent(View view);
    }

    void registerListener(NewTopicToPresenter newTopicToPresenter);
    void unregisterListener();
    //void takePhoto(View view);
}
