package com.bimapp.view.interfaces;

import android.graphics.Bitmap;
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
    void setImage(Bitmap image);
    void postedTopic(Topic topic);

    //void takePhoto(View view);
}
