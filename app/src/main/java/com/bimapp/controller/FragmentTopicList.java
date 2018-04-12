package com.bimapp.controller;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.TopicsFragmentInterface;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.entityManagers.TopicsEntityManager;
import com.bimapp.view.TopicsListView;
import com.bimapp.view.interfaces.TopicsViewInterface;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTopicList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTopicList extends Fragment
        implements TopicsFragmentInterface , TopicsViewInterface.TopicsViewToPresenter{

    // Interface for the view, setListener makes this the callback.
    // private TopicViewInterface mTopicViewInterface;

    // Used to get topics from the model. Could potentially be implemented in this class,
    // but probably better to separate them.
    private TopicsEntityManager mTopicsEntityManager;

    private TopicsViewInterface mTopicsView;

    private TopicSelectionInterface mListener;
    // Getting the application
    private BimApp mApplication;

    public FragmentTopicList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FragmentTopicList.
     */
    public static FragmentTopicList newInstance(TopicSelectionInterface listener) {
        FragmentTopicList fragment = new FragmentTopicList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (BimApp) this.getActivity().getApplication();
        mTopicsEntityManager = new TopicsEntityManager(mApplication);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Instatiate the view
        mTopicsView = new TopicsListView(inflater, container);
        // Set this as the callback from the view
        mTopicsView.registerListener(this);
        mTopicsEntityManager.getTopics(this);
        // Inflate the layout for this fragment
        return mTopicsView.getRootView();
    }

    @Override
    public void onAttach(Context context){
        // Doesn't do anything at the moment, but we might find uses for it!
        super.onAttach(context);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mTopicsEntityManager = null; // I think this helps prevent memory leaks
    }

    /**
     * From TopicsFragmentsInterface.
     * Calls on a method in the view and gives the topics the view should show.
     * @param topics the topics to be listed
     */
    @Override
    public void setTopics(List<Topic> topics) {
        // This method shall do a callback to the view when it has recieved topics
        mTopicsView.setTopics(topics);
    }

    @Override
    public void onSelectedItem(Topic topic) {
        mListener.onTopicSelected(topic);
    }

    private void setListener(TopicSelectionInterface listener){
        mListener = listener;
    }

    public interface TopicSelectionInterface{
        void onTopicSelected(Topic topic);
    }
}