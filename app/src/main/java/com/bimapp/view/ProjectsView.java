package com.bimapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.R;
import com.bimapp.model.entity.Project;
import com.bimapp.view.interfaces.ProjectsViewInterface;

import java.util.List;

/**
 * Created by Håkon on 20.03.2018.
 */

public class ProjectsView implements ProjectsViewInterface {

    private View mRootView;
    private ShowProjectsViewListener mListener;

    public ProjectsView(LayoutInflater inflater, ViewGroup container){
        mRootView = inflater.inflate(R.layout.fragment_projects_view, container);

    }


    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    /**
     * from ProjectsViewInterface. sets the implementation for this listener.
     * @param projectsView the implementation of the ShowProjectsViewListener interface
     */
    @Override
    public void registerListener(ShowProjectsViewListener projectsView) {
        mListener = projectsView;
    }
    /**
     * from ProjectsViewInterface. removes the implementation for this listener.
     **/
    @Override
    public void unregisterListener() {
        mListener = null;
    }

    /**
     * Adds the projects from the network to the projects view.
     * @param projects a List of projects.
     */
    @Override
    public void setProjects(List<Project> projects) {

    }
}
