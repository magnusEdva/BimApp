package com.bimapp.controller;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.TopicsFragmentInterface;
import com.bimapp.model.data_access.DataProvider;
import com.bimapp.model.data_access.entityManagers.TopicsEntityManager;
import com.bimapp.model.entity.Topic;
import com.bimapp.view.TopicsListView;
import com.bimapp.view.interfaces.TopicsViewInterface;

import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTopicList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTopicList extends Fragment
        implements TopicsFragmentInterface, TopicsViewInterface.TopicsViewToPresenter {

    // Interface for the view, setListener makes this the callback.
    // private TopicViewInterface mTopicViewInterface;

    // Used to get topics from the model. Could potentially be implemented in this class,
    // but probably better to separate them.
    private TopicsEntityManager mTopicsEntityManager;

    private TopicsViewInterface mTopicsView;

    private TopicSelectionInterface mListener;
    // Getting the application
    private BimApp mApplication;

    private static String searchQuery;

    private static String searchArg;

    public FragmentTopicList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FragmentTopicList.
     */
    public static FragmentTopicList newInstance() {
        FragmentTopicList fragment = new FragmentTopicList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (BimApp) this.getActivity().getApplication();
        mTopicsEntityManager = new TopicsEntityManager(mApplication);


        if (mTopicsView != null)
            mTopicsView.clearSearch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Instatiate the view
        mTopicsView = new TopicsListView(inflater, container);
        // Set this as the callback from the view
        mTopicsView.registerListener(this);
        // Inflate the layout for this fragment
        mTopicsView.clearSearch();
        return mTopicsView.getRootView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TopicSelectionInterface)
            mListener = (TopicSelectionInterface) context;
        else
            throw new UnsupportedOperationException();


    }

    @Override
    public void onResume() {
        super.onResume();

        mTopicsEntityManager.getTopics(this);

        mTopicsView.clearSearch();

        if (searchForAssignedTo()) {
            onSearch(searchArg, searchQuery, false);
        } else mTopicsView.search();


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTopicsEntityManager = null;
        mListener = null;
    }

    /**
     * From TopicsFragmentsInterface.
     * Calls on a method in the view and gives the topics the view should show.
     *
     * @param topics the topics to be listed
     */
    @Override
    public void setTopics(List<Topic> topics) {
        if (topics != null && !topics.isEmpty()) {
            if (searchArg != null && searchQuery != null)
                onSearch(searchArg, searchQuery, false);
            else
                mTopicsView.setTopics(topics);
        }

    }

    @Override
    public void setSearchResult(List<Topic> topic) {
        mTopicsView.setSearchResult(topic);
    }

    private boolean searchForAssignedTo() {
        return (searchQuery != null && searchArg != null);
    }

    @Override
    public void onSearch(String argument, String searchString, boolean deleteArgs) {
        mTopicsEntityManager.searchTopics(this, argument, searchString);

        if (deleteArgs) {
            searchQuery = null;
            searchArg = null;
        }
    }


    /**
     * @param selection specified element to look for
     * @param arg       column for the selection
     */
    public void setFilterArgs(String selection, String arg) {
        searchArg = arg;
        searchQuery = selection;
    }


    /**
     *
     */
    public void loadAllTopics() {

    }

    @Override
    public void onSelectedItem(Topic topic) {
        mListener.onTopicSelected(topic);
    }

    public interface TopicSelectionInterface {
        void onTopicSelected(Topic topic);
    }

}
