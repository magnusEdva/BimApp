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

public interface entity {
    /**
     * This returns all relevant data from the entity to be sent.
     * @return Object to be sent with a request.
     */
    JSONObject getJsonParams();
}
