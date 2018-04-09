package com.bimapp.view.interfaces;

import com.bimapp.model.entity.Template.Template;

import java.util.List;

/**
 * Created by zorri on 09/04/2018.
 */

public interface DashboardViewInterface extends ViewMVP {

    interface DashboardViewListener {
        void onSelectedItem(Template template);
    }
    void onInteraction(Template template);
    void registerListener(DashboardViewListener projectsView);
    void unregisterListener();
    void setTemplates(List<Template> template);
}
