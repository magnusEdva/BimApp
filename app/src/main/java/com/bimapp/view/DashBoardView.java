package com.bimapp.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.R;
import com.bimapp.view.interfaces.ViewMVP;

/**
 * Created by Håkon on 23.03.2018.
 */

public class DashBoardView implements ViewMVP {

    public interface DashBoardNavDrawerViewListener {
        /**
         * Will be called when Navigation Bars visibility changes
         *
         * @param isVisible
         */
        void onDrawerVisibilityStateChanged(boolean isVisible);

        /**
         * Will be called when "navigate up" button is called
         */
        void onNavigationClick();
    }


    @NonNull
    private final AppCompatActivity mActivity;
    private View mRootView;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    public DashBoardView(@NonNull LayoutInflater inflater,
                         @NonNull ViewGroup container,
                         @NonNull AppCompatActivity activity) {
        mActivity = activity;
        mRootView = inflater.inflate((R.layout.activity_dash_board), container);

        //init();
    }

    private void init() {
        initToolbar();
        initNavDrawer();
    }


    private void initToolbar() {
        mToolbar = (Toolbar) getRootView().findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(mToolbar);
    }

    /**
     * Initiate the navigation drawer
     */
    private void initNavDrawer() {

        mDrawerLayout = (DrawerLayout) getRootView().findViewById(R.id.drawer_layout);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                mActivity,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close) {

            private boolean mIsDrawerVisibleLast = false;
/*
            @Override
            public void onDrawerSlide(View view, float v) {

                // Only update when drawer's visibility actually changed
                if (mIsDrawerVisibleLast != isDrawerVisible()) {
                    mIsDrawerVisibleLast = !mIsDrawerVisibleLast;
                    for (MainNavDrawerViewMvcListener listener : getListeners()) {
                        listener.onDrawerVisibilityStateChanged(mIsDrawerVisibleLast);
                    }
                }
            }*/
        };

/*
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        mActionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (MainNavDrawerViewMvcListener listener : getListeners()) {
                    listener.onNavigationClick();
                }
            }
        });
*/
        // This is required because of a bug. More info:
        //http://stackoverflow.com/questions/26549008/missing-up-navigation-icon-after-switching-from-ics-actionbar-to-lollipop-toolbar
        if (mActivity.getDrawerToggleDelegate() != null) mActionBarDrawerToggle
                .setHomeAsUpIndicator(mActivity.getDrawerToggleDelegate().getThemeUpIndicator());

        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

    }


    @Override
    public View getRootView() {
        return null;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    public void setTitle(String title) {
        mToolbar.setTitle(title);
    }

    /**
     * See {@link ActionBarDrawerToggle#syncState()}
     */
    public void syncDrawerToggleState() {
        mActionBarDrawerToggle.syncState();
    }

    /**
     * See {@link ActionBarDrawerToggle#setDrawerIndicatorEnabled(boolean)}
     *
     * @param enabled true un order to show drawer's indicator ("hamburger"); false in order to
     *                show "up" navigation icon
     */
    public void setDrawerIndicatorEnabled(boolean enabled) {
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(enabled);
    }

    public boolean isDrawerVisible() {
        return mDrawerLayout.isDrawerVisible(GravityCompat.START);
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void hideToolbar() {
        mToolbar.setVisibility(View.GONE);
    }

    public void showToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
    }
}