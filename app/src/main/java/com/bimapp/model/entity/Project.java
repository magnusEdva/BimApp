package com.bimapp.model.entity;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Model implementation of a the project type.
 */

public class Project implements Entity {
    private String projectId;
    private String bimsyncProjectName;
    private String name;
    private String bimsyncProjectId;

    /**
     * Used when building a project from network.
     * @param project is JSONObject containing a project.
     */
    public Project(JSONObject project) {
        construct(project);
    }

    /**
     * Used to build a Project from a series of String. Useful for acquiring
     * ActiveProject from storage.
     * @param projectId String with a valid projectId.
     * @param bimsyncProjectName  String with a valid projectName corresponding to the IDs.
     * @param bimsyncProjectId String with the BimsyncProjectId corresponding to the project Id
     * @param name String with the project name corresponding to the project Id.
     */
    public Project(String projectId, String bimsyncProjectName, String bimsyncProjectId, String name){
        this.projectId = projectId;
        this.bimsyncProjectName = bimsyncProjectName;
        this.bimsyncProjectId = bimsyncProjectId;
        this.name = name;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getBimsyncProjectName() {
        return bimsyncProjectName;
    }

    public String getName() {
        return name;
    }

    public String getBimsyncProjectId() {
        return bimsyncProjectId;
    }

    @Override
    public Map<String, String> getParams(@NonNull Map<String, String> map) {
        map.put("project_id", projectId);
        map.put("bimsync_project_name", bimsyncProjectName);
        map.put("name", name);
        map.put("bimsync_project_id", bimsyncProjectId);
        return map;
    }


    private void construct(JSONObject object) {
        try {
            projectId = object.getString("project_id");
            name = object.getString("name");
            bimsyncProjectName = object.getString("bimsync_project_name");
            bimsyncProjectId = object.getString("bimsync_project_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return bimsyncProjectName;
    }
}
