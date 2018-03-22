package com.bimapp.view.interfaces;

import com.bimapp.model.entity.Project;

import java.util.List;

/**
 * Interface defining the contracts between the View and the Presenter.
 * <p>
 * The {@link ProjectsViewInterface} interface is implemented by the Presenter, and is called from the View
 * when something is selected by the view.
 * <p>
 * The other methods are implemented by the View.
 * The {@link registerListener} method lets the View know which method to callback with {@link onSelectedItem}.
 * The {@link setProjects} method is called by the Presenter when it has received a list of projects.
 */

public interface ProjectsViewInterface extends ViewMVP {

    interface ShowProjectsViewListener {
        void onSelectedItem(Project project);
    }

    void registerListener(ShowProjectsViewListener projectsView);
    void unregisterListener();
    void setProjects(List<Project> projects);
}
