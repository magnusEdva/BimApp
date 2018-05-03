package com.bimapp.view.interfaces;

import android.graphics.Bitmap;
import android.view.View;

import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.entity.Viewpoint;


/**
 * Interface implemented by the View
 */
public interface NewTopicViewInterface extends ViewMVP, View.OnClickListener {


    void updateExtensionsDefaultValue();

    /**
     * Interface implemented by the presenter
     */
    interface NewTopicToPresenter{
        void onPostTopic(Topic topic, Comment mComment, Viewpoint vp);
        void onCameraIntent(View view);
    }

    void registerListener(NewTopicToPresenter newTopicToPresenter);
    void unregisterListener();
    void setImage(Bitmap image);
}
