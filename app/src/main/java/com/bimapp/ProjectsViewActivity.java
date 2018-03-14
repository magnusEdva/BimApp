package com.bimapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.bimapp.model.entity.Project;
import com.bimapp.model.network.Callback;
import com.bimapp.model.network.GetUser;
import com.bimapp.view.fragments.ProjectsFragment;



public class ProjectsViewActivity extends AppCompatActivity implements ProjectsFragment.OnListFragmentInteractionListener{

    BimApp mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        mApplication = (BimApp) getApplication();

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onListFragmentInteraction(Project project) {
        mApplication.deleteTokens();
    }


}
