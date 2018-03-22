package com.bimapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bimapp.R;
import com.bimapp.model.entity.Topic;

import java.util.List;

/**
 * Created by Hakon on 22.03.2018.
 */

public class TopicsView implements TopicsViewInterface {

    private View mRootView;
    private TopicsViewToPresenter mListener;

    public TopicsView(LayoutInflater inflater, ViewGroup container) {
        mRootView = inflater.inflate(R.layout.fragment_topics_view, container);
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
        ArrayAdapter<Topic> arrayAdapter = new ArrayAdapter<>(this.getRootView().getContext(), android.R.layout.simple_list_item_1, topics);
        ListView listView = mRootView.findViewById(R.id.topics_list);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
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
}
