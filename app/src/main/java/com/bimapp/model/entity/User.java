package com.bimapp.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * User entity class. As defined in API.
 */

@Entity(tableName = "user")
public class User implements entity {
    /*

     */
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;
    @ColumnInfo(name = "name")
    private String name;

    public static final String ID = "id";
    public static final String NAME = "name";

    /**
     *
     * @param obj contains parameter keys "id" and "name", specifies a user object.
     */
    public User(JSONObject obj){
        construct(obj);
    }

    public User(){}

    public User(String id, String name){
        this.id = id;
        this.name = name;
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
            id = object.getString(ID);
            name = object.getString(NAME);
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

    public void setName (String name) {this.name = name;}

    public void setId (String id) {this.id = id;}

}
