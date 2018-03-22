package com.bimapp.view.interfaces;

import com.bimapp.model.entity.Project;

import java.util.List;

/**
 * Created by Håkon on 20.03.2018.
 */

public interface ProjectsViewInterface extends ViewMVP {

    interface ShowProjectsViewListener{
        void onSelectedItem(Project project);
    }

    void registerListener(ShowProjectsViewListener projectsView);

    void unregisterListener();

    void setProjects(List<Project> projects);

}
