package com.bimapp.view.interfaces;

/**
 * Created by Håkon on 20.03.2018.
 */

public interface ProjectsInterface extends ViewMVP {

    interface ShowProjectsViewListener{
        void onSelectedItem();
    }

    void SetListener(ShowProjectsViewListener projectsView);

}
