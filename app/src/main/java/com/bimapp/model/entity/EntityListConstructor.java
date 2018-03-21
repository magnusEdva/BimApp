package com.bimapp.model.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which uses Entity implementations to generate Entity Arrays from JSONArray.
 */

public class EntityListConstructor {
    private EntityListConstructor(){}

    /**
     * aqcuires a Project [] from JSONArray
     * @param projectsJSON contains properly formatted Project entities.
     * @return Actual Project[] of @param PojectsJSON
     */
    public static List<Project> Projects(JSONArray projectsJSON){
        List<Project> projects = new ArrayList<>();

        for(int i = 0; i < projectsJSON.length(); i++){
            try {
                projects.add(new Project(projectsJSON.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return projects;
    }

    public static List<Topic> constructTopics(JSONArray topicsJSON){
        List<Topic> topics = new ArrayList<>();

        for(int i = 0; i < topicsJSON.length(); i++){
            try {
                topics.add(new Topic(topicsJSON.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return topics;
    }
}
