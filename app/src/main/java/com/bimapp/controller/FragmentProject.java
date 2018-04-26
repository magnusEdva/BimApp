package com.bimapp.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.ProjectsFragmentInterface;
import com.bimapp.model.entity.IssueBoardExtensions;
import com.bimapp.model.entity.Project;
import com.bimapp.model.entityManagers.IssueBoardExtensionsEntityManager;
import com.bimapp.model.entityManagers.ProjectEntityManager;
import com.bimapp.view.ProjectsView;
import com.bimapp.view.interfaces.ProjectsViewInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass that creates a view that shows a list of projects in a ListView.
 * <p>
 * Activities that contain this fragment must implement the
 * {@link OnFragmentProjectInteractionListener} interface to handle interaction events.
 */
public class FragmentProject extends Fragment
        implements ProjectsFragmentInterface, ProjectsViewInterface.ShowProjectsViewListener, IssueBoardExtensionsEntityManager.IssueBoardExtensionsProjectCallback {

    /*
    The implementation of the View
     */
    private ProjectsViewInterface mProjectsView;
    private ProjectEntityManager mProjectsManager;
    private BimApp mContext;
    private OnFragmentProjectInteractionListener mCallback;
    private IssueBoardExtensionsEntityManager mExtensionManager;
    private Project mProject;


    private List<Project> mProjects;
    private boolean BCF;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (BimApp) this.getActivity().getApplication();
        mProjectsManager = new ProjectEntityManager(mContext);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        if (mProjectsView == null)
            mProjectsView = new ProjectsView(inflater, container);
        /**
         Sets this as the callback from the view. See {@link onSelectedItem} method for where the callback goes.
         */
        mProjectsView.registerListener(this);

        return mProjectsView.getRootView();
    }

    @Override
    public void onResume() {
        super.onResume();
        mProjectsManager.getProjects(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (OnFragmentProjectInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mProjectsManager = null;

    }

    /**
     * Callback method from the view.
     * Starts new activity/fragment based on which project was selected. (Currently just logs it)
     * Also sets the active project in the application.
     *
     * @param project the selected project
     */
    @Override
    public void onSelectedItem(Project project) {
        if (BCF) {
            mProject = project;
            mExtensionManager = new IssueBoardExtensionsEntityManager(mContext);
            mExtensionManager.getIssueBoardExtensions(project, this);
            Log.d("ID : ", project.getProjectId());
            BCF = false;
        } else {
            BCF = true;
            mProjectsView.setProjects(makeIssueBoardList(project));
        }
    }

    /**
     * From the ProjectsFragmentsInterface.
     * Calls on a method in the view and gives the projects the view should show.
     *
     * @param projects the projects to be listed
     */
    @Override
    public void setProjects(List<Project> projects) {
        mProjects = projects;
        mProjectsView.setProjects(makeProjectList());
    }

    @Override
    public void setExtensions(IssueBoardExtensions issueBoardExtensions) {
        mProject.setIssueBoardExtensions(issueBoardExtensions);
        mContext.setActiveProject(mProject);
        mCallback.onFragmentProjectInteraction(mProject);

    }

    private List<Project> makeIssueBoardList(Project issueBoard) {
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < mProjects.size(); i++) {
            if (mProjects.get(i).getBimsyncProjectId().equals(issueBoard.getBimsyncProjectId())) {
                projects.add(mProjects.get(i));
                projects.get(projects.size() - 1).setState(false);
            }
        }
        return projects;
    }

    /**
     * removes all Bimsync Project duplicates from the List
     *
     * @return only the actual projects, no issue board duplicates.
     */
    private List<Project> makeProjectList() {
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < mProjects.size(); i++) {
            boolean exists = false;
            for (int j = 0; j < projects.size() && !exists; j++) {
                if (mProjects.get(i).getBimsyncProjectId().equals(projects.get(j).getBimsyncProjectId()))
                    exists = true;
            }
            if (!exists) {
                projects.add(mProjects.get(i));
                projects.get(projects.size() - 1 ).setState(true);
            }
        }
        return projects;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentProjectInteractionListener {
        // TODO: Update argument type and name
        void onFragmentProjectInteraction(Project project);
    }
}
