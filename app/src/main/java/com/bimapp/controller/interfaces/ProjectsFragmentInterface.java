package com.bimapp.controller.interfaces;

import com.bimapp.model.entity.Project;

import java.util.List;

/**
 * Interface implemented by
 */

public interface ProjectsFragmentInterface {
    interface FragmentProjectListener {
        void getProjects(ProjectsFragmentInterface callback);
    }
    void setProjects(List<Project> projects);
}
