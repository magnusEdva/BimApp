package com.bimapp.model.entity;

import android.support.annotation.NonNull;

import com.bimapp.model.entity.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * A topic is a model class which can be used to showcase a problem
 * or something else.
 */

public class Topic implements Entity {

    private String mGuid;

    private String mTopicType;

    private String mTopicStatus;

    private List<String> mReferenceLinks;
    /**
     * title of the topic. Used to quickly identify what the topic is about.
     */
    private String mTitle;

    private String mPriority;

    private Integer mIndex;

    private String mCreationDate;

    private String mCreationAuthor;

    private String mModifiedAuthor;

    private String mAssignedTo;

    private String mStage;
    /**
     * actual description of the Topic. Used with the title to provide full context.
     */
    private String mDescription;

    //private String bim_snippet;

    private String mDueDate;

    public Topic(JSONObject obj) {
        construct(obj);
    }

    public String getmGuid() {
        return mGuid;
    }

    public String getmTopicType() {
        return mTopicType;
    }

    public String getmTopicStatus() {
        return mTopicStatus;
    }

    public List<String> getmReferenceLinks() {
        return mReferenceLinks;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmPriority() {
        return mPriority;
    }

    public Integer getmIndex() {
        return mIndex;
    }

    public String getmCreationDate() {
        return mCreationDate;
    }

    public String getmCreationAuthor() {
        return mCreationAuthor;
    }

    public String getmModifiedAuthor() {
        return mModifiedAuthor;
    }

    public String getmAssignedTo() {
        return mAssignedTo;
    }

    public String getmStage() {
        return mStage;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmDueDate() {
        return mDueDate;
    }

    private void construct(JSONObject obj) {

        try {
            if (obj.has("guid"))
                mGuid = obj.getString("guid");
            if (obj.has("topic_type"))
                mTopicType = obj.getString("topic_type");
            if (obj.has("topic_status"))
                mTopicStatus = obj.getString("topic_status");
            //TODO aquire an actual array mReferenceLinks = obj.getJSONArray("reference_links");
            if (obj.has("title"))
                mTitle = obj.getString("title");
            if (obj.has("description"))
                mDescription = obj.getString("description");
            if (obj.has("priority"))
                mPriority = obj.getString("priority");
            if (obj.has("index"))
                mIndex = obj.getInt("index");
            if (obj.has("creation_date"))
                mCreationDate = obj.getString("creation_date");
            if (obj.has("creation_author"))
                mCreationAuthor = obj.getString("creation_author");
            if (obj.has("modified_author"))
                mModifiedAuthor = obj.getString("modified_author");
            if (obj.has("assigned_to"))
                mAssignedTo = obj.getString("assigned_to");
            if (obj.has("stage"))
                mStage = obj.getString("stage");

            //TODO bimSnippet = obj.getString("bim_snippet");
            if (obj.has("due_date"))
                mDueDate = obj.getString("due_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param map @NonNull empty or non-empty map that is used to store the parameters
     * @return map containing all of this topics variables.
     */
    @Override
    public Map<String, String> getParams(@NonNull Map<String, String> map) {
        map.put("guid", mGuid);
        map.put("topic_type", mTopicType);
        map.put("topic_status", mTopicStatus);
        //TODO map.put("reference_links", mReferenceLinks);
        map.put("title", mTitle);
        map.put("priority", mPriority);
        map.put("index", mIndex.toString());
        map.put("creation_date", mCreationDate);
        map.put("creation_author", mCreationAuthor);
        map.put("modified_author", mModifiedAuthor);
        map.put("assigned_to", mAssignedTo);
        map.put("stage", mStage);
        map.put("description", mDescription);
        //TODO map.put("bim_snippet, mBimSnippet);
        map.put("due_date", mDueDate);
        return map;
    }

    @Override
    public String toString() {
        return mTitle + " " + mDescription;
    }
}
