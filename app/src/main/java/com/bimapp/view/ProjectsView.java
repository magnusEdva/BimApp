package com.bimapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bimapp.R;
import com.bimapp.model.entity.Project;
import com.bimapp.view.interfaces.ProjectsViewInterface;

import java.util.List;

/**
 * Created by Håkon on 20.03.2018.
 */

public class ProjectsView implements ProjectsViewInterface {

    private ListView mRootView;
    private ShowProjectsViewListener mListener;

    public ProjectsView(LayoutInflater inflater, ViewGroup container){
        mRootView = (ListView) inflater.inflate(R.layout.fragment_projects_view, container);
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

        ArrayAdapter<Project> arrayAdapter = new ArrayAdapter<Project>(this.getRootView().getContext(), R.layout.project_simpler_layout,projects);
        ListView listView = (ListView) mRootView.findViewById(R.id.project_list);
        listView.setAdapter(arrayAdapter);

    }
}
