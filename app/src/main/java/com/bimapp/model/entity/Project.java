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
    String projectId;
    String bimsyncProjectName;
    String name;
    String bimsyncProjectId;

    public Project(JSONObject project) {
        construct(project);
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
        return name;
    }
}
