package com.bimapp.model.entity;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Model implementation of a the project type.
 */

public class Project implements Entity {
    public static final String PROJECT_ID = "project_id";
    public static final String BIMSYNC_PROJECT_NAME = "bimsync_project_name";
    public static final String NAME = "name";
    public static final String BIMSYNC_PROJECT_ID = "bimsync_project_id";


    private String projectId;
    private String bimsyncProjectName;
    private String name;
    private String bimsyncProjectId;

    private IssueBoardExtensions mIssueBoardExtensions;

    /**
     * Used when building a project from network.
     * @param project is JSONObject containing a project.
     */
    public Project(JSONObject project) {
        construct(project);
    }

    /**
     * Used to build a Project from a series of String. Useful for acquiring
     * ActiveProject from storage.
     * @param projectId String with a valid projectId.
     * @param bimsyncProjectName  String with a valid projectName corresponding to the IDs.
     * @param bimsyncProjectId String with the BimsyncProjectId corresponding to the project Id
     * @param name String with the project name corresponding to the project Id.
     */
    public Project(String projectId, String bimsyncProjectName, String bimsyncProjectId, String name, IssueBoardExtensions issueBoardExtensions){
        this.projectId = projectId;
        this.bimsyncProjectName = bimsyncProjectName;
        this.bimsyncProjectId = bimsyncProjectId;
        this.name = name;
        this.mIssueBoardExtensions = issueBoardExtensions;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getBimsyncProjectName() {
        return bimsyncProjectName;
    }

    public String getName() {
        return name;
    }

    public String getBimsyncProjectId() {
        return bimsyncProjectId;
    }

    public IssueBoardExtensions getIssueBoardExtensions() {
        return mIssueBoardExtensions;
    }

    public void setIssueBoardExtensions(IssueBoardExtensions issueBoardExtensions){
        mIssueBoardExtensions = issueBoardExtensions;
    }

    /**
     *
     * @param map @NonNull
     * @return always throws exception
     */
    @Override
    public Map<String, String> getStringParams(@NonNull Map<String, String> map) {
        throw new UnsupportedOperationException();
    }

    /**
     * Method to create new project. Not used in this app
     * @return
     */
    @Override
    public JSONObject getJsonParams() {
        throw new UnsupportedOperationException();
    }


    private void construct(JSONObject object) {
        try {
            projectId = object.getString(PROJECT_ID);
            name = new String(object.getString(NAME).getBytes("ISO-8859-1"), "UTF-8");
            bimsyncProjectName = object.getString(BIMSYNC_PROJECT_NAME);
            bimsyncProjectId = object.getString(BIMSYNC_PROJECT_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return name + " (" +bimsyncProjectName + ")";
    }


}
