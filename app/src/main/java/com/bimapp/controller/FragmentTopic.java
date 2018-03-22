package com.bimapp.controller;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTopic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTopic extends Fragment {


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_topic, container, false);
    }

}
