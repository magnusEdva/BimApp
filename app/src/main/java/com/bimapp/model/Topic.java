package com.bimapp.model;

import android.support.annotation.NonNull;

import com.bimapp.model.entity.Entity;

import java.util.Map;

/**
 * A topic is a model class which can be used to showcase a problem
 * or something else.
 */

public class Topic implements Entity {

    

    /**
     * @param map @NonNull empty or non-empty map that is used to store the parameters
     * @return map containing all of this topics variables.
     */
    @Override
    public Map<String, String> getParams(@NonNull Map<String, String> map) {
        return null;
    }
}
