package com.bimapp.controller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.BimApp;
import com.bimapp.R;
import com.bimapp.controller.interfaces.ProjectsFragmentrInterface;
import com.bimapp.model.entityManagers.ProjectEntityManager;
import com.bimapp.view.ProjectsView;
import com.bimapp.view.interfaces.ProjectsViewInterface;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProjectsViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class ProjectsViewFragment extends Fragment implements ProjectsViewInterface.ShowProjectsViewListener , ProjectsFragmentrInterface.ProjectsListener{

    /*
    The implementation of the View
     */
    private ProjectsView mProjectsView;
    private ProjectEntityManager mProjectsManager;
    private BimApp mContext;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mContext = (BimApp) getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mProjectsManager = new ProjectEntityManager(mContext);
        mProjectsManager.getProjects();

        mProjectsView = new ProjectsView(inflater,container);
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_projects_view, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mProjectsManager = null;
    }

    @Override
    public void onSelectedItem() {

    }

    @Override
    public void onProjectSelected(String projectId) {

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
