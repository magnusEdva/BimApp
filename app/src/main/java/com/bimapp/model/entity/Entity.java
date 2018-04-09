package com.bimapp.model.entity;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

/**
 * Inteface for all supported model classes.
 * Contains the necessary functions for network communcation
 * including acquiring necessary parameters for API requests.
 */

public interface Entity{
    /**
     * Takes a map and populates it with the implementations parameters
     * Used exclusively for GET requests.
     * @param map @NonNull
     * @return same map with model parameters
     */
    Map<String,String> getStringParams(@NonNull Map<String,String> map);

    /**
     * This returns all relevant data from the Entity to be sent.
     * @return Object to be sent with a request.
     */
    JSONObject getJsonParams();
}
