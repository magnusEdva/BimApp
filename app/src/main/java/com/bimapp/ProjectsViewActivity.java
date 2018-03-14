package com.bimapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bimapp.view.fragments.ProjectsFragment;
import com.bimapp.view.fragments.dummy.DummyContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProjectsViewActivity extends AppCompatActivity implements ProjectsFragment.OnListFragmentInteractionListener {

    BimApp mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        mApplication = (BimApp) getApplication();

    }


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        mApplication.deleteTokens();
    }
}
