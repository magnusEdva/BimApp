package com.bimapp.model.entity;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

/**
 * User entity class. As defined in API.
 */

public class User implements Entity {
    /*

     */
    private String id;
    private String name;

    /**
     *
     * @param obj contains parameter keys "id" and "name", specifies a user object.
     */
    public User(JSONObject obj){
        construct(obj);
    }

    /**
     * not used.
     * @param map @NonNull an empty map.
     * @return null value
     */
    @Override
    public Map<String, String> getStringParams(@NonNull Map<String, String> map) {
            throw new UnsupportedOperationException();
    }

    /**
     * not used
     * @return only throws exception.
     */
    @Override
    public JSONObject getJsonParams() {
            throw new UnsupportedOperationException();
    }

    /**
     * used in the constructor. Builds a user object from JSON.
     * @param object
     */
    private void construct(JSONObject object) {
        try {
            id = object.getString("id");
            name = object.getString("name");
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }


}
