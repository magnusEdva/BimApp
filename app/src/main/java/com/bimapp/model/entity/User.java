package com.bimapp.model.entity;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

/**
 * Created by Hakon on 15.03.2018.
 */

public class User implements Entity {
    /*

     */
    private String createdAt; // This is a DATE format
    private String id;
    private String name;
    private String username; // Documentation says this is both added and not added!

    public User(){

    }

    @Override
    public Map<String, String> getParams(@NonNull Map<String, String> map) {
        // Should never ADD a user, don't need to do anything here
        return null;
    }

    @Override
    public Entity construct(JSONObject object) {
        try {
            createdAt = object.getString("createdAt");
            id = object.getString("id");
            name = object.getString("name");
            username = object.getString("username");
        } catch (JSONException e){
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public Entity[] construct(JSONArray array) {
        User[] users = null;
        try{
            users = new User[array.length()];
            for (int i = 0; i < array.length(); i++) {
                users[i] = new User();
                users[i].construct(array.getJSONObject(i));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public String toString(){
        return name;
    }

    //No reason to have setters, right?
    public String getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}
