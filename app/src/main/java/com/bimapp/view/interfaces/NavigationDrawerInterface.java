package com.bimapp.view.interfaces;

import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.View;

/**
 * Interface defining the contract between the NavigationDrawer and the DashBoard activity.
 *
 * The {@link NavigationDrawerListener}interface is implemented by the {@link com.bimapp.controller.DashBoard}
 */

public interface NavigationDrawerInterface extends ViewMVP, NavigationView.OnNavigationItemSelectedListener {

    interface NavigationDrawerListener{
        void onNavigationDrawerItemSelected(Menu item);
    }
    void registerListener(NavigationDrawerInterface navigationDrawerInterface);
    void unregisterListener();
    void setFragmentView(View view);
}
