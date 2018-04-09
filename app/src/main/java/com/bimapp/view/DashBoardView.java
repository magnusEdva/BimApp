package com.bimapp.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.R;
import com.bimapp.controller.DashboardAdapter;
import com.bimapp.model.entity.Template.Template;
import com.bimapp.view.interfaces.DashboardViewInterface;

import java.util.List;

/**
 * Created by Håkon on 23.03.2018.
 */

public class DashBoardView implements DashboardViewInterface {


    private View mRootView;
    private Toolbar mToolbar;
    private DashboardViewListener mListener;
    private DashboardAdapter mAdapter;


    public DashBoardView(@NonNull LayoutInflater inflater,
                         @NonNull ViewGroup container) {

        mRootView = inflater.inflate((R.layout.fragment_dashboard_list), container, false);

    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }


    @Override
    public void onInteraction(Template template) {
        mListener.onSelectedItem(template);
    }

    @Override
    public void registerListener(DashboardViewListener listener) {
        mListener = listener;
    }

    @Override
    public void unregisterListener() {
        mListener = null;
    }

    @Override
    public void setTemplates(List<Template> template) {

        RecyclerView recyclerView = mRootView.findViewById(R.id.list);

    }
}