package com.bimapp.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.bimapp.BimApp;
import com.bimapp.R;
import com.bimapp.model.data_access.DataProvider;
import com.bimapp.model.entity.Topic;
import com.bimapp.view.interfaces.TopicsViewInterface;

import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Hakon on 22.03.2018.
 */

public class TopicsListView implements TopicsViewInterface {

    private View mRootView;

    private BimApp mContext;

    private TopicsViewToPresenter mListener;

    private SearchView searchString;

    public TopicsListView(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.view_topics_list, container, false);
        searchString = mRootView.findViewById(R.id.topics_list_search_editText);
        mContext = (BimApp) mRootView.getContext().getApplicationContext();
        setupSearchButton();
    }

    @Override
    public void registerListener(TopicsViewToPresenter topicsViewToPresenter) {
        mListener = topicsViewToPresenter;
    }

    @Override
    public void unregisterListener() {
        mListener = null;
    }

    @Override
    public void setTopics(final List<Topic> topics) {
        if (searchString.getQuery() == null) {
            searchString.setQuery(searchString.getQuery(), true);
        }else {
            ArrayAdapter<Topic> arrayAdapter = new ArrayAdapter<>(this.getRootView().getContext(), android.R.layout.simple_list_item_1, topics);
            ListView listView = mRootView.findViewById(R.id.topics_list);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                    InputMethodManager inputMethodManager = (InputMethodManager) mRootView.getContext().
                            getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(mRootView.getWindowToken(), 0);
                    mListener.onSelectedItem(topics.get(position));
                }
            });
        }
    }

    @Override
    public void setSearchResult (final List<Topic> topics){
        ArrayAdapter<Topic> arrayAdapter = new ArrayAdapter<>(this.getRootView().getContext(), android.R.layout.simple_list_item_1, topics);
        ListView listView = mRootView.findViewById(R.id.topics_list);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                InputMethodManager inputMethodManager = (InputMethodManager) mRootView.getContext().
                        getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mRootView.getWindowToken(), 0);
                mListener.onSelectedItem(topics.get(position));
            }
        });
    }
    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    private void setupSearchButton() {
        searchString.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (mContext.getActiveProject().getIssueBoardExtensions().getUserIdType().contains(query))
                    mListener.onSearch(DataProvider.ASSIGNED_TO, query);
                else
                    mListener.onSearch(DataProvider.SEARCH, searchString.getQuery().toString());
                searchString.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (mContext.getActiveProject().getIssueBoardExtensions().getUserIdType().contains(newText))
                    mListener.onSearch(DataProvider.ASSIGNED_TO, newText);
                else
                    mListener.onSearch(DataProvider.SEARCH, searchString.getQuery().toString());
                return true;
            }
        });
    }

    @Override
    public void setSearchString(String searchString) {
        this.searchString.setQuery(searchString, false);
        this.searchString.clearFocus();
    }

    @Override
    public void search() {
        searchString.setQuery(searchString.getQuery(), true);
    }

    @Override
    public void clearSearch() {
        searchString.setQuery("", false);
        searchString.clearFocus();
    }
}
