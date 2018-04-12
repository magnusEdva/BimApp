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
     * @return Actual Project[] of @param projectsJSON
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

    /**
     * Aquires a Topics [] from JSONArray
     * @param topicsJSON contains properly formatted Topic entities
     * @return actual Topics[] of @param topicsJSON
     */
    public static List<Topic> Topics(JSONArray topicsJSON){
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

    /**
     * @param commentsJSON JSON [] containing comments
     * @return Comment objects in a List
     */
    public static List<Comment> Comments(JSONArray commentsJSON){
        List<Comment> comments = new ArrayList<>();

        for(int i = 0; i < commentsJSON.length(); i++){
            try {
                comments.add(new Comment(commentsJSON.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return comments;
    }
}
