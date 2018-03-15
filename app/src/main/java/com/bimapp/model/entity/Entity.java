package com.bimapp.model.entity;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * Inteface for all supported model classes.
 * Contains the necessary functions for network communcation
 * including construction and acquiring necessary parameters
 * for API requests.
 */

public abstract class Entity <T> {
    /**
     * Takes a map and populates it with the implementations parameters
     * @param map @NonNull
     * @return same map with model parameters
     */
    abstract Map<T,T> getParams(@NonNull Map<T,T> map);

    /**
     * constructs the model with it's corresponding JSONObject
     * @param object needs to actually contain the correct entity
     * @return the created Entity
     */
    abstract Entity construct(JSONObject object);
    /**
     * constructs the list with it's corresponding JSONArray
     * @param array needs to actually contain the correct entity
     * @return the created Entity
     */
    abstract Entity [] construct(JSONArray array);
}
