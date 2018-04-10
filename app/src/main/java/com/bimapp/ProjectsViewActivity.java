package com.bimapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;
import com.bimapp.controller.FragmentDashboard;
import com.bimapp.controller.FragmentNewTopic;
import com.bimapp.controller.FragmentProject;
import com.bimapp.controller.FragmentTopic;
import com.bimapp.model.entity.Project;
import com.bimapp.model.entity.Template.Template;
import com.bimapp.model.entity.User;
import com.bimapp.model.network.APICall;
import com.bimapp.model.network.Callback;
import com.bimapp.model.network.NetworkConnManager;

import org.json.JSONException;
import org.json.JSONObject;


public class ProjectsViewActivity extends AppCompatActivity
        implements
        Callback, FragmentProject.OnFragmentProjectInteractionListener, FragmentDashboard.DashboardListener {

    private BimApp mApplication;
    private DrawerLayout mDrawerLayout;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        mApplication = (BimApp) getApplication();

        //This is a terrible way to handle things! It essentially gets the
        //user and sets the user variable through a callback method
        // Should be moved to some BimApp setting on login
        NetworkConnManager.networkRequest(mApplication, Request.Method.GET,
        APICall.GETUser(), this, null);

    }

    @Override
    public void onResume(){
        super.onResume();

        //Setting toolbar as the actionbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);


        // Defines the drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        final FragmentManager fragmentManager = ProjectsViewActivity.this.getSupportFragmentManager();
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // set item as selected to persist highlight
                        //item.setChecked(true);
                        // Close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        int id = item.getItemId();

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Fragment fragment = null;

                        switch (id){
                            case R.id.nav_projects:
                                fragment =  new FragmentProject(); // new ProjectsFragment();
                                fragmentTransaction.replace(R.id.fragments_container, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                break;
                            case R.id.nav_issues:
                                fragment = new FragmentTopic();
                                fragmentTransaction.replace(R.id.fragments_container, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                break;
                            case R.id.nav_dashboard:
                                fragment = new FragmentDashboard();
                                fragmentTransaction.replace(R.id.fragments_container, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                break;
                            case R.id.nav_new_topic:
                                fragment = new FragmentNewTopic();
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

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new FragmentDashboard();
        fragmentTransaction.replace(R.id.fragments_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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

    @Override
    public void onFragmentProjectInteraction(Project project) {
        Log.d("Clicked on project: ", project.getName());

    }

    @Override
    public void onDashboardItemClick(Template template) {
        final FragmentManager fragmentManager = ProjectsViewActivity.this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new FragmentNewTopic();
        fragmentTransaction.replace(R.id.fragments_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
