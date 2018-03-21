package com.bimapp.controller.interfaces;

import com.bimapp.model.entity.Project;

import java.util.List;

/**
 * Created by Håkon on 20.03.2018.
 */

public interface ProjectsFragmentInterface {


    interface ProjectsListener {
        void gotProjects(List<Project> projects, ProjectsFragmentInterface callback);
        void getProjects(ProjectsFragmentInterface callback);

    }

    void registerListener(ProjectsListener listener);

    void unRegisterListener();

    void setProjects(List<Project> projects);
}
