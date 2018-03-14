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

public class Project implements Entity<String> {
    private Date createdAt;
    private String id;
    private String name;
    private Date updatedAt;

    public Project() {
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
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

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
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

    @Override
    public Entity construct(JSONObject object) {
        try {
            id = object.getString("id");
            name = object.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public Entity[] construct(JSONArray array) {
        Project[] projects = null;
        try {
            projects = new Project[array.length()];
            for (int i = 0; i < array.length(); i++) {
                projects[i] = new Project();
                projects[i].construct(array.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return projects;
    }

    @Override
    public String toString(){
        return name;
    }
}
