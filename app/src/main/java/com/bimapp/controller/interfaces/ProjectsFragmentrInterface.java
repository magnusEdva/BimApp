package com.bimapp.controller.interfaces;

/**
 * Created by Håkon on 20.03.2018.
 */

public interface ProjectsFragmentrInterface {


    interface ProjectsListener {
        void onProjectSelected(String projectId);
    }

    void registerListener(ProjectsListener listener);

    void unRegisterListener();
}
