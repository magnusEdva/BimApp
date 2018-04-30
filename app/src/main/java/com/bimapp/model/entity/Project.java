package com.bimapp.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Model implementation of a the project type.
 */
@Entity(tableName = "project")
public class Project implements entity {
    public static final String PROJECT_ID = "project_id";
    public static final String BIMSYNC_PROJECT_NAME = "bimsync_project_name";
    public static final String NAME = "name";
    public static final String BIMSYNC_PROJECT_ID = "bimsync_project_id";
    public static final String ISSUE_BOARD_EXTENSIONS = "issue_board_extensions";
    /**
     * used by the presenter to control which name the view
     * shows. either bimsyncProjectName or name, depending on state.
     */
    @Ignore
    private boolean state;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = PROJECT_ID)
    private String projectId;
    @ColumnInfo(name = BIMSYNC_PROJECT_NAME)
    private String bimsyncProjectName;
    @ColumnInfo(name = NAME)
    private String name;
    @ColumnInfo(name = BIMSYNC_PROJECT_ID)
    private String bimsyncProjectId;
    @Embedded
    private IssueBoardExtensions mIssueBoardExtensions;

    /**
     * Used when building a project from network.
     *
     * @param project is JSONObject containing a project.
     */
    public Project(JSONObject project) {
        construct(project);
    }

    /**
     * Used to build a Project from a series of String. Useful for acquiring
     * ActiveProject from storage.
     *
     * @param projectId          String with a valid projectId.
     * @param bimsyncProjectName String with a valid projectName corresponding to the IDs.
     * @param bimsyncProjectId   String with the BimsyncProjectId corresponding to the project Id
     * @param name               String with the project name corresponding to the project Id.
     */
    public Project(@NonNull String projectId, String bimsyncProjectName, String bimsyncProjectId, String name, IssueBoardExtensions issueBoardExtensions) {
        this.projectId = projectId;
        this.bimsyncProjectName = bimsyncProjectName;
        this.bimsyncProjectId = bimsyncProjectId;
        this.name = name;
        this.mIssueBoardExtensions = issueBoardExtensions;
    }

    /**
     * recreates this instance in the DataProvider before being inserted into the database.
     *
     * @param values
     */
    public Project(ContentValues values) {
        projectId = values.getAsString(PROJECT_ID);
        name = values.getAsString(NAME);
        bimsyncProjectName = values.getAsString(BIMSYNC_PROJECT_NAME);
        bimsyncProjectId = values.getAsString(BIMSYNC_PROJECT_ID);
        JSONObject obj = new JSONObject();
        mIssueBoardExtensions = new IssueBoardExtensions();
        try {
            if(values.getAsString(IssueBoardExtensions.USER_ID_TYPE) != null)
                obj.put(IssueBoardExtensions.USER_ID_TYPE, new JSONArray(values.getAsString(IssueBoardExtensions.USER_ID_TYPE)));
            if(values.getAsString(IssueBoardExtensions.TOPIC_LABELS) != null)
                obj.put(IssueBoardExtensions.TOPIC_LABELS, new JSONArray(values.getAsString(IssueBoardExtensions.TOPIC_LABELS)));
            if(values.getAsString(IssueBoardExtensions.TOPIC_STATUS) != null)
                obj.put(IssueBoardExtensions.TOPIC_STATUS, new JSONArray(values.getAsString(IssueBoardExtensions.TOPIC_STATUS)));
            if(values.getAsString(IssueBoardExtensions.TOPIC_TYPE) != null)
                obj.put(IssueBoardExtensions.TOPIC_TYPE, new JSONArray(values.getAsString(IssueBoardExtensions.TOPIC_TYPE)));


        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void setIssueBoardExtensions(IssueBoardExtensions issueBoardExtensions) {
        mIssueBoardExtensions = issueBoardExtensions;
    }

    /**
     * Method for acquiring all the project Types and get them with the
     * topics Type in position 0 for Spinner purposes.
     *
     * @param topic this topics type is in pos 0
     * @return project Types
     */

    public List<String> getProjectTypesOrdered(Topic topic) {
        String topicType = topic.getMTopicType();
        List<String> projectTypes = mIssueBoardExtensions.getTopicType();
        return reOrderList(projectTypes, topicType);
    }

    /**
     * Method for acquiring all the project Status and get them with the
     * topics Status in position 0 for Spinner purposes.
     *
     * @param topic this topics Status is in pos 0
     * @return project Statuses
     */
    public List<String> getProjectStatusOrdered(Topic topic) {
        String Status = topic.getMTopicStatus();
        List<String> projectStatus = mIssueBoardExtensions.getTopicStatus();
        return reOrderList(projectStatus, Status);
    }

    private List<String> reOrderList(List<String> list, String newFirstElement) {
        String tempStringStorage = list.get(0);
        list.remove(0);
        list.add(0, newFirstElement);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).equals(newFirstElement)) {
                list.remove(i);
                list.add(i, tempStringStorage);

            }
        }
        return list;
    }

    /**
     * Method to create new project. Not used in this app
     *
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
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(PROJECT_ID, projectId);
        values.put(NAME, name);
        values.put(BIMSYNC_PROJECT_ID, bimsyncProjectId);
        values.put(BIMSYNC_PROJECT_NAME, bimsyncProjectName);

        if (mIssueBoardExtensions != null) {
            values.put(IssueBoardExtensions.TOPIC_TYPE, Topic.getStringFromList(mIssueBoardExtensions.getTopicType()));
            values.put(IssueBoardExtensions.TOPIC_LABELS, Topic.getStringFromList(mIssueBoardExtensions.getTopicLabel()));
            values.put(IssueBoardExtensions.TOPIC_STATUS, Topic.getStringFromList(mIssueBoardExtensions.getTopicStatus()));
            values.put(IssueBoardExtensions.USER_ID_TYPE, Topic.getStringFromList(mIssueBoardExtensions.getUserIdType()));
        }
        return values;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        if (state) return bimsyncProjectName;
        return name;
    }
}
