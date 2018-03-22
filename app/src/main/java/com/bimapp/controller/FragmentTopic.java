package com.bimapp.controller;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.BimApp;
import com.bimapp.R;
import com.bimapp.controller.interfaces.TopicsFragmentInterface;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.entityManagers.TopicsEntityManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTopic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTopic extends Fragment implements TopicsFragmentInterface {

    // Interface for the view, setListener makes this the callback.
    // private TopicViewInterface mTopicViewInterface;

    // Used to get topics from the model. Could potentially be implemented in this class,
    // but probably better to separate them.
    private TopicsEntityManager mTopicsEntityManager;

    // Getting the application
    private BimApp mApplication;

    public FragmentTopic() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FragmentTopic.
     */
    public static FragmentTopic newInstance() {
        FragmentTopic fragment = new FragmentTopic();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (BimApp) this.getActivity().getApplication();
        mTopicsEntityManager = new TopicsEntityManager(mApplication);
        mTopicsEntityManager.getTopics(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Instatiate the view
        // mTopicsView = new TopicsView(inflater, container);
        // Set this as the callback from the view
        // mTopicsView.registerListener(this);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_topic, container, false);
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
        // mTopicsView.setTopics(topics);
    }

    // Implement override method from the view that tells this controller which topic was selected
}