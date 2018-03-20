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
    private String createdAt;
    private String id;
    private String name;
    private String updatedAt;

    public Project(JSONObject project) {
        construct(project);
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public Map<String, String> getParams(@NonNull Map<String, String> map) {
        map.put("createdAt", "");
        map.put("id", id);
        map.put("name", name);
        map.put("updatedAt", "");
        return map;
    }


    private void construct(JSONObject object) {
        try {
            id = object.getString("id");
            name = object.getString("name");
            updatedAt = object.getString("updatedAt");
            createdAt = object.getString("createdAt");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return name;
    }
}
