package com.bimapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.R;
import com.bimapp.view.interfaces.ProjectsViewInterface;

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
    public void SetListener(ShowProjectsViewListener projectsViewListener) {
        mListener = projectsViewListener;
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }
}
