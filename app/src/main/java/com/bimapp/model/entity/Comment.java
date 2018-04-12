package com.bimapp.model.entity;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Comment belonging to a Topic. This Class belongs to said topic,
 * but the topic itself need not have any reference to these.
 */
public class Comment implements Entity {
    public static final String GUID = "";
    public static final String VERBAL_STATUS = "verbal_status";
    public static final String STATUS = "status";
    public static final String DATE = "date";
    public static final String AUTHOR = "author";
    public static final String COMMENT = "comment";
    public static final String TOPIC_GUID = "topic_guid";
    public static final String MODIFIED_DATE = "modified_date";
    public static final String MODIFIED_AUTHOR = "modified_author";

    /**
     * Unique identifier for a comment.
     */
    private String mCommentsGUID;
    /**
     * comment verbal status
     */
    private String mVerbalStatus;
    /**
     * comment status
     */
    private String mStatus;
    /**
     * creation date
     */
    private String mDate;
    /**
     * comment author
     */
    private String mAuthor;
    /**
     * topic id
     */
    private String mTopicGUID;
    /**
     * modified at
     */
    private String mModifiedDate;
    /**
     * modified by
     */
    private String mModifiedAuthor;
    /**
     * actual comment content
     */
    private String mComment;


    public Comment(JSONObject obj) {
        construct(obj);
    }

    /**
     * constructs a comment from a correspong JSON file
     *
     * @param obj JSON file
     */
    private void construct(JSONObject obj) {
        try {
            if (obj.has(GUID))
                mCommentsGUID = obj.getString(GUID);
            if (obj.has(TOPIC_GUID))
                mTopicGUID = obj.getString(TOPIC_GUID);
            if (obj.has(VERBAL_STATUS))
                mVerbalStatus = obj.getString(VERBAL_STATUS);
            if (obj.has(STATUS))
                mStatus = obj.getString(STATUS);
            if (obj.has(DATE))
                mDate = obj.getString(DATE);
            if (obj.has(AUTHOR))
                mAuthor = obj.getString(AUTHOR);
            if (obj.has(COMMENT))
                mComment = obj.getString(COMMENT);
            if (obj.has(MODIFIED_DATE))
                mModifiedDate = obj.getString(MODIFIED_DATE);
            if (obj.has(MODIFIED_AUTHOR))
                mModifiedAuthor = obj.getString(MODIFIED_AUTHOR);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param map @NonNull
     * @return
     */
    @Override
    public Map<String, String> getStringParams(@NonNull Map<String, String> map) {
        return null;
    }

    @Override
    public JSONObject getJsonParams() {
        JSONObject map = new JSONObject();
        try {
            map.put(STATUS, mStatus);
            throw new UnsupportedOperationException();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    public String toString() {
        return mComment;
    }

}
