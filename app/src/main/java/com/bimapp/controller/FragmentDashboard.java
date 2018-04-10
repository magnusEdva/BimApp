package com.bimapp.controller;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.model.entity.Template.Template;
import com.bimapp.view.DashBoardView;
import com.bimapp.view.interfaces.DashboardViewInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment representing a list of Items.
 * <p/>
 * * interface.
 */
public class FragmentDashboard extends Fragment implements DashboardViewInterface.DashboardViewListener {


    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;
    private DashboardViewInterface mDashboardView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentDashboard() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FragmentDashboard newInstance(int columnCount) {
        FragmentDashboard fragment = new FragmentDashboard();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDashboardView = new DashBoardView(inflater,container);
        mDashboardView.registerListener(this);
        MOCKTEMPLATES MOCK = new MOCKTEMPLATES();
        List<Template> temps = new ArrayList<>();
        temps.add(new Template(MOCK.templateOne));
        temps.add(new Template(MOCK.templateTwo));

        mDashboardView.setTemplates(temps);

        return mDashboardView.getRootView();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDashboardView.unregisterListener();
    }

    @Override
    public void onSelectedItem(Template template) {
        Log.d("click", template.getTitle());
    }

    public class MOCKTEMPLATES{
        JSONObject templateOne;

        JSONObject templateTwo;

        public  MOCKTEMPLATES(){
            try{
                templateTwo = new JSONObject();
                templateOne = new JSONObject();

                templateOne.put(Template.TITLE, "title");
                templateOne.put(Template.DESCRIPTION, "description BLA BLA BLA BLA LBA BLA BLA BLA");
                templateOne.put(Template.COLOR, Color.BLUE);
                templateOne.put(Template.ICON, 1);


                templateTwo.put(Template.TITLE, "Title Two");
                templateTwo.put(Template.DESCRIPTION, "description Two BLA BLA BLA BLA LBA BLA BLA BLA");
                templateTwo.put(Template.COLOR, Color.RED);
                templateTwo.put(Template.ICON, 2);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
