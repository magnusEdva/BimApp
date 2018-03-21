package com.bimapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.volley.Request;
import com.bimapp.controller.FragmentViewProject;
import com.bimapp.model.entity.Project;
import com.bimapp.model.entity.User;
import com.bimapp.model.network.Callback;
import com.bimapp.model.network.NetworkConnManager;
import com.bimapp.fragmentsold.ProjectsFragment;
import com.bimapp.fragmentsold.UserFragment;
import com.bimapp.fragmentsold.dummy.DummyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ProjectsViewActivity extends AppCompatActivity implements ProjectsFragment.OnListFragmentInteractionListener, UserFragment.OnListFragmentInteractionListener , Callback{

    private BimApp mApplication;
    private DrawerLayout mDrawerLayout;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        mApplication = (BimApp) getApplication();

        //Somehow I have a feeling this is a terrible way to handle things! It essentially gets the
        //user and sets the user variable through a callback method
        NetworkConnManager.networkRequest(mApplication, Request.Method.GET,
        NetworkConnManager.APICall.GETUser, this, null);



        //Setting toolbar as the actionbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);


        // Defines the drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // set item as selected to persist highlight
                        //item.setChecked(true);
                        // Close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        int id = item.getItemId();
                        FragmentManager fragmentManager = ProjectsViewActivity.this.getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Fragment fragment;

                        switch (id){
                            case R.id.nav_projects:
                                fragment = (Fragment) new FragmentViewProject(); // new ProjectsFragment();
                                fragmentTransaction.replace(R.id.fragments_container, fragment);
                                fragmentTransaction.addToBackStack(null);

                                fragmentTransaction.commit();
                                break;
                            case R.id.nav_issues:
                                fragment = new UserFragment();
                                fragmentTransaction.replace(R.id.fragments_container, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                break;
                            case R.id.nav_log_out:
                                mApplication.logOut();
                                Intent intent = new Intent(ProjectsViewActivity.this, WelcomeActivity.class);
                                //TODO Make sure that when this is called, the BackStack is cleared somehow!
                                startActivity(intent);
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    @Override
    public void onResume(){
        super.onResume();
        //ProjectsFragment fragment = (ProjectsFragment ) this.getFragmentManager().findFragmentById(R.id.projects_fragment);
        //fragment.loadProjects();
    }

    @Override
    public void onListFragmentInteraction(Project project) {


    }


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onError(String response) {

    }

    @Override
    public void onSuccess(String response) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        user = new User(obj);
        Log.d("Created user", user.getName());
        TextView textView = findViewById(R.id.nav_header_title);
        textView.setText(user.getName());
    }
}
