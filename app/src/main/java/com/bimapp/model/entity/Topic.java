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
        try{
            mGuid = obj.getString("guid");
            mTopicType = obj.getString("topic_type");
            mTopicStatus = obj.getString("topic_status");
            //TODO aquire an actual array mReferenceLinks = obj.getJSONArray("reference_links");
            mTitle = obj.getString("title");
            mPriority = obj.getString("priority");
            mIndex = obj.getInt("index");
            mCreationDate = obj.getString("creation_date");
            mCreationAuthor = obj.getString("creation_author");
            mModifiedAuthor = obj.getString("modified_author");
            mAssignedTo = obj.getString("assigned_to");
            mStage = obj.getString("stage");
            mDescription = obj.getString("description");
            //TODO bimSnippet = obj.getString("bim_snippet");
            mDueDate = obj.getString("due_date");
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * @param map @NonNull empty or non-empty map that is used to store the parameters
     * @return map containing all of this topics variables.
     */
    @Override
    public Map<String, String> getParams(@NonNull Map<String, String> map) {
        map.put("guid",mGuid);
        map.put("topic_type",mTopicType);
        map.put("topic_status",mTopicStatus);
        //TODO map.put("reference_links", mReferenceLinks);
        map.put("title",mTitle);
        map.put("priority",mPriority);
        map.put("index",mIndex.toString());
        map.put("creation_date",mCreationDate);
        map.put("creation_author",mCreationAuthor);
        map.put("modified_author",mModifiedAuthor);
        map.put("assigned_to",mAssignedTo);
        map.put("stage",mStage);
        map.put("description",mDescription);
        //TODO map.put("bim_snippet, mBimSnippet);
        map.put("due_date",mDueDate);
        return map;
    }

    @Override
    public String toString(){
        return mTitle + " " + mDescription;
    }
}
