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
    private DashboardListener mListener;


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
        mDashboardView = new DashBoardView(inflater, container);
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
        if (context instanceof DashboardListener)
            mListener = (DashboardListener) context;
        else
            throw new UnsupportedOperationException();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDashboardView.unregisterListener();
        mListener = null;
    }

    @Override
    public void onSelectedItem(Template template) {
        mListener.onDashboardItemClick(template);
    }

    public interface DashboardListener {
        public void onDashboardItemClick(Template template);
    }


    public class MOCKTEMPLATES {
        JSONObject templateOne;

        JSONObject templateTwo;

        public MOCKTEMPLATES() {
            try {
                templateTwo = new JSONObject("{\n" +
                        "    \"templateName\": \"Uønsket hendelse\",\n" +
                        "    \"templateDescription\": \"\",\n" +
                        "    \"templateColor\": \"#f45f41\",\n" +
                        "    \"title\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": null\n" +
                        "    },\n" +
                        "    \"description\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": \"Rapport ved uønsket hendelse\"\n" +
                        "    },\n" +
                        "    \"topic_status\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": \"Open\"\n" +
                        "    },\n" +
                        "    \"topic_type\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": \"Incident\"\n" +
                        "    },\n" +
                        "    \"labels\": {\n" +
                        "        \"mandatory\": false,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": [\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"assigned_to\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": false,\n" +
                        "        \"defaultValue\": \"chief@company.com\"\n" +
                        "    },\n" +
                        "    \"stage\": {\n" +
                        "        \"mandatory\": false,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": \"Bygge fase 1\"\n" +
                        "    },\n" +
                        "    \"due_date\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": false,\n" +
                        "        \"defaultValue\": \"2018-12-05T00:00:00+01:00\"\n" +
                        "    },\n" +
                        "    \"image\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true\n" +
                        "    },\n" +
                        "    \"comment\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": null\n" +
                        "    }\n" +
                        "}");
                templateOne = new JSONObject("\n" +
                        "{\n" +
                        "    \"templateName\": \"Inspeksjonsrunde\",\n" +
                        "    \"templateDescription\": \"test description\",\n" +
                        "    \"templateColor\": \"#4286f4\",\n" +
                        "    \"description\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": \"Sjekkliste inspeksjonsrunde\"\n" +
                        "    },\n" +
                        "    \"title\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": null\n" +
                        "    },\n" +
                        "    \"topic_status\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": false,\n" +
                        "        \"defaultValue\": \"Open\"\n" +
                        "    },\n" +
                        "    \"topic_type\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": false,\n" +
                        "        \"defaultValue\": \"Inspection\"\n" +
                        "    },\n" +
                        "    \"labels\": {\n" +
                        "        \"mandatory\": false,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": [\n" +
                        "            \"Tidlig fase\",\n" +
                        "            \"Byggesone 1\"\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"assigned_to\": {\n" +
                        "        \"mandatory\": false,\n" +
                        "        \"visible\": false,\n" +
                        "        \"defaultValue\": null\n" +
                        "    },\n" +
                        "    \"stage\": {\n" +
                        "        \"mandatory\": false,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": \"Bygge fase 1\"\n" +
                        "    },\n" +
                        "    \"due_date\": {\n" +
                        "        \"mandatory\": false,\n" +
                        "        \"visible\": false,\n" +
                        "        \"defaultValue\": null\n" +
                        "    },\n" +
                        "    \"image\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true\n" +
                        "    },\n" +
                        "    \"comment\": {\n" +
                        "        \"mandatory\": false,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": null\n" +
                        "    }\n" +
                        "}");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
