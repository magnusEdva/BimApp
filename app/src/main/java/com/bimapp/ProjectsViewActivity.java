package com.bimapp;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.bimapp.model.entity.Project;
import com.bimapp.model.network.Callback;
import com.bimapp.model.network.GetUser;
import com.bimapp.view.fragments.ProjectsFragment;
import com.bimapp.view.fragments.UserFragment;
import com.bimapp.view.fragments.dummy.DummyContent;


public class ProjectsViewActivity extends AppCompatActivity implements ProjectsFragment.OnListFragmentInteractionListener, UserFragment.OnListFragmentInteractionListener {

    private BimApp mApplication;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        mApplication = (BimApp) getApplication();

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

                        switch (id){
                            case R.id.projects:

                                ProjectsFragment fragment = (ProjectsFragment ) ProjectsViewActivity.this.getFragmentManager().findFragmentById(R.id.projects_fragment);
                                fragment.loadProjects();
                                break;
                            case R.id.user:
                                UserFragment userFragment = (UserFragment) ProjectsViewActivity.this.getFragmentManager().findFragmentById(R.id.fragment_user);
                                userFragment.loadUser();
                                break;
                            case R.id.log_out:
                                mApplication.deleteTokens();
                                Intent intent = new Intent(ProjectsViewActivity.this, WelcomeActivity.class);
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
}
