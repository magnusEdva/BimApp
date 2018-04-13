package com.bimapp.controller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.BimApp;
import com.bimapp.R;
import com.bimapp.controller.interfaces.ProjectsFragmentInterface;
import com.bimapp.model.entity.Project;
import com.bimapp.model.entityManagers.ProjectEntityManager;
import com.bimapp.view.ProjectsView;
import com.bimapp.view.interfaces.ProjectsViewInterface;

import java.util.List;

/**
 * A simple {@link Fragment} subclass that creates a view that shows a list of projects in a ListView.
 *
 * Activities that contain this fragment must implement the
 * {@link OnFragmentProjectInteractionListener} interface to handle interaction events.
 */
public class FragmentProject extends Fragment
        implements ProjectsFragmentInterface, ProjectsViewInterface.ShowProjectsViewListener{

    /*
    The implementation of the View
     */
    private ProjectsViewInterface mProjectsView;
    private ProjectEntityManager mProjectsManager;
    private BimApp mApplication;
    private OnFragmentProjectInteractionListener mCallback;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mApplication = (BimApp) this.getActivity().getApplication();
        mProjectsManager = new ProjectEntityManager(mApplication);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        if(mProjectsView == null)
            mProjectsView = new ProjectsView(inflater,container);
        /**
        Sets this as the callback from the view. See {@link onSelectedItem} method for where the callback goes.
         */
        mProjectsView.registerListener(this);

        return mProjectsView.getRootView();
    }

    @Override
    public void onResume(){
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
     * @param project the selected project
     */
    @Override
    public void onSelectedItem(Project project) {

        mApplication.setActiveProject(project);
        mCallback.onFragmentProjectInteraction(project);
        Log.d("ID : ", project.getProjectId());
    }

    /**
     * From the ProjectsFragmentsInterface.
     * Calls on a method in the view and gives the projects the view should show.
     * @param projects the projects to be listed
     */
    @Override
    public void setProjects(List<Project> projects) {
        mProjectsView.setProjects(projects);
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
