package com.bimapp.model.entity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A topic is a model class which can be used to showcase a problem
 * or something else.
 */

public class Topic implements Entity {
    public static String GUID = "guid";
    public static String TOPIC_TYPE = "topic_type";
    public static String TOPIC_STATUS = "topic_status";
    public static String REFERENCE_LINKS = "reference_links";
    public static String TITLE = "title";
    public static String PRIORITY = "priority";
    public static String INDEX = "index";
    public static String LABELS = "labels";
    public static String CREATION_DATE = "creation_date";
    public static String CREATION_AUTHOR = "creation_author";
    public static String MODIFIED_DATE = "modified_date";
    public static String MODIFIED_AUTHOR = "modified_author";
    public static String ASSIGNED_TO = "assigned_to";
    public static String STAGE = "stage";
    public static String DESCRIPTION = "description";
    public static String BIM_SNIPPET = "bim_snippet";
    public static String DUE_DATE = "due_date";
    public static String AUTHORIZATION = "authorization";


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

    private List<String> mLabels;

    private String mStage;
    /**
     * actual description of the Topic. Used with the title to provide full context.
     */
    private String mDescription;

    private BimSnippet mBimSnippet;

    private String mDueDate;


    public Topic(JSONObject obj) {
        construct(obj);
    }

    public Topic(@NonNull String title, @Nullable String topicType, @Nullable String topicStatus,
                 @Nullable String assignedTo, @Nullable String description) {
        mTitle = title;
        mTopicType = topicType;
        mTopicStatus = topicStatus;
        mAssignedTo = assignedTo;
        mDescription = description;
    }

    public String getGuid() {
        return mGuid;
    }

    public String getTopicType() {
        return mTopicType;
    }

    public void setTopicType(String topicType) {
        mTopicType = topicType;
    }

    public String getTopicStatus() {
        return mTopicStatus;
    }

    public void setTopicStatus(String topicStatus) {
        mTopicStatus = topicStatus;
    }

    public List<String> getReferenceLinks() {
        return mReferenceLinks;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getPriority() {
        return mPriority;
    }

    public Integer getIndex() {
        return mIndex;
    }

    public String getCreationDate() {
        return mCreationDate;
    }

    public String getCreationAuthor() {
        return mCreationAuthor;
    }

    public String getModifiedAuthor() {
        return mModifiedAuthor;
    }

    public String getAssignedTo() {
        return mAssignedTo;
    }

    public String getStage() {
        return mStage;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getDueDate() {
        return mDueDate;
    }

    private void construct(JSONObject obj) {

        try {
            if (obj.has(GUID))
                mGuid = obj.getString(GUID);
            if (obj.has(TOPIC_TYPE))
                mTopicType = obj.getString(TOPIC_TYPE);
            if (obj.has(TOPIC_STATUS))
                mTopicStatus = obj.getString(TOPIC_STATUS);
            if (obj.has(LABELS))
                mLabels = getListFromJSonArray(obj.getJSONArray(LABELS));
            if (obj.has(REFERENCE_LINKS))
                mReferenceLinks = getListFromJSonArray(obj.getJSONArray(REFERENCE_LINKS));
            if (obj.has(TITLE))
                mTitle = obj.getString(TITLE);
            if (obj.has(DESCRIPTION))
                mDescription = obj.getString(DESCRIPTION);
            if (obj.has(PRIORITY))
                mPriority = obj.getString(PRIORITY);
            if (obj.has(INDEX))
                mIndex = obj.getInt(INDEX);
            if (obj.has(CREATION_DATE))
                mCreationDate = obj.getString(CREATION_DATE);
            if (obj.has(CREATION_AUTHOR))
                mCreationAuthor = obj.getString(CREATION_AUTHOR);
            if (obj.has(MODIFIED_AUTHOR))
                mModifiedAuthor = obj.getString(MODIFIED_AUTHOR);
            if (obj.has(ASSIGNED_TO))
                mAssignedTo = obj.getString(ASSIGNED_TO);
            if (obj.has(STAGE))
                mStage = obj.getString(STAGE);
            if(obj.has(BIM_SNIPPET))
             mBimSnippet = new BimSnippet(obj.getJSONObject("bim_snippet"));
            if (obj.has(DUE_DATE))
                mDueDate = obj.getString(DUE_DATE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param map @NonNull empty or non-empty map that is used to store the parameters
     * @return map containing all of this topics variables.
     */
    @Override
    public Map<String, String> getStringParams(@NonNull Map<String, String> map) {
        if (mTopicType != null)
            map.put("topic_type", "");
        if (mTopicStatus != null)
            map.put("topic_status", "");
        //TODO map.put("reference_links", mReferenceLinks);
        map.put("title", mTitle);
        if (mPriority != null)
            map.put("priority", mPriority);
        if (mIndex != null)
            map.put("index", mIndex.toString());
        if (mAssignedTo != null)
            map.put("assigned_to", "");
        if (mStage != null)
            map.put("stage", mStage);
        if (mDescription != null)
            map.put("description", mDescription);
        if (mDueDate != null)
            map.put("due_date", mDueDate);
        return map;
    }

    @Override
    public JSONObject getJsonParams() {
        JSONObject map = new JSONObject();
        try {
            if (mTopicType != null)
                map.put(TOPIC_TYPE, mTopicType);
            if (mTopicStatus != null)
                map.put(TOPIC_STATUS, mTopicStatus);
            if (mReferenceLinks != null)
                map.put(REFERENCE_LINKS, mReferenceLinks);
            if (mLabels != null)
                map.put(LABELS, mLabels);
            if (mTitle != null)
                map.put(TITLE, mTitle);
            if (mPriority != null)
                map.put(PRIORITY, mPriority);
            if (mIndex != null)
                map.put(INDEX, mIndex.toString());
            if (mAssignedTo != null)
                map.put(ASSIGNED_TO, mAssignedTo);
            if (mStage != null)
                map.put(STAGE, mStage);
            if (mDescription != null)
                map.put(DESCRIPTION, mDescription);
            if(mBimSnippet != null)
                map.put(BIM_SNIPPET, mBimSnippet.getJSON());
            if (mDueDate != null)
                map.put(DUE_DATE, mDueDate);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    public List<String> getListFromJSonArray(JSONArray array) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                strings.add(array.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return strings;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    private class BimSnippet {
        final static String SNIPPET_TYPE = "snippet_type";
        final static String IS_EXTERNAL = "is_external";
        final static String REFERENCE = "reference";
        final static String REFERENCE_SCHEMA = "reference_schema";

        String mSnippet_type;
        boolean mExternal;
        String mReference;
        String mReferenceSchema;

        public BimSnippet(JSONObject snippet) {
            try {
                if (snippet.has(SNIPPET_TYPE))
                    mSnippet_type = snippet.getString(SNIPPET_TYPE);
                if (snippet.has(IS_EXTERNAL))
                    mExternal = snippet.getBoolean(IS_EXTERNAL);
                if (snippet.has(REFERENCE))
                    mReference = snippet.getString(REFERENCE);
                if (snippet.has(REFERENCE_SCHEMA))
                    mReferenceSchema = snippet.getString(REFERENCE_SCHEMA);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        public JSONObject getJSON(){
            JSONObject map = new JSONObject();
            try {
                map.put(SNIPPET_TYPE, mSnippet_type);
                map.put(IS_EXTERNAL, mExternal);
                map.put(REFERENCE, mReference);
                map.put(REFERENCE_SCHEMA, mReferenceSchema);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return map;
        }
    }

}
