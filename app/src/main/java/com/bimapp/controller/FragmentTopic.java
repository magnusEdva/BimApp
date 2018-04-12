package com.bimapp.controller;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.R;
import com.bimapp.model.entity.Topic;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTopic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTopic extends Fragment {


    public FragmentTopic() { }

    public static FragmentTopic newInstance(Topic topic) {
        FragmentTopic fragment = new FragmentTopic();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_topic, container, false);
    }



}
