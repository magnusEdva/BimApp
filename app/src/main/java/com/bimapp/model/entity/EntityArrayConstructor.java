package com.bimapp.model.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class which uses Entity implementations to generate Entity Arrays from JSONArray.
 */

public class EntityArrayConstructor {
    private EntityArrayConstructor(){}

    /**
     * aqcuires a Project [] from JSONArray
     * @param projectsJSON contains properly formatted Project entities.
     * @return Actual Project[] of @param PojectsJSON
     */
    public static Project [] constructProjects(JSONArray projectsJSON){
        Project [] projects = new Project[projectsJSON.length()];

        for(int i = 0; i < projectsJSON.length(); i++){
            try {
                projects[i] = new Project(projectsJSON.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return projects;
    }
}
