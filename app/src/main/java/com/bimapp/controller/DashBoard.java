package com.bimapp.controller;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.BimApp;
import com.bimapp.R;
import com.bimapp.view.interfaces.NavigationDrawerInterface;

/**
 * Class to serve as the dashboard. Will serve as the "home" screen for users as soon as they are logged in.
 */
public class DashBoard extends AppCompatActivity {

    // Implement interfaces this activity shall have interaction with. (Basically all of them)

    private BimApp mApplication;
    // Add listeners this activity shall interact with.
    private NavigationDrawerInterface mNavigationDrawerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplication = (BimApp) this.getApplication();
        setContentView(R.layout.activity_dash_board);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }


    /**
     * Method for handling interaction with the navigation drawer.
     * Shall start certain fragments depending on which item is selected.
     *
     * @param item the navigation item that was clicked
     * @return
     */
//    @Override
    public void onNavigationDrawerItemSelected(Menu item) {
        //TODO Do something!
    }
}
