package com.bimapp.controller.interfaces;

import com.bimapp.model.entity.Topic;

/**
 * Interface to communicate between the {@link com.bimapp.controller.FragmentNewTopic} responsible
 * for allowing users to post new topics, and the
 * {@link com.bimapp.model.DataAccess.entityManagers.TopicsEntityManager} which is responsible for
 * posting the topic to the API.
 *
 * {@link com.bimapp.controller.FragmentNewTopic} must implement this interface
 */
public interface NewTopicFragmentInterface {
    /**
     * This interface is implemented by the {@link com.bimapp.model.DataAccess.entityManagers.TopicsEntityManager}
     * and
     */
    interface NewTopicFragmentListener{
        /**
         * Method telling the {@link com.bimapp.model.DataAccess.entityManagers.TopicsEntityManager} to post a
         * topic to the api. Called from {@link com.bimapp.controller.FragmentNewTopic}
         * @param topic The topic you want to post
         */
        void postTopic(Topic topic);
    }

    /**
     * Method implemented by {@link com.bimapp.controller.FragmentNewTopic} and is called when
     * {@link com.bimapp.model.DataAccess.entityManagers.TopicsEntityManager} has finished posting a topic.
     */
    void postedTopic(boolean success, Topic topic);
}
