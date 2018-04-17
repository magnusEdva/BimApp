package com.bimapp.model.entity;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
    public static final String VIEWPOINT_GUID = "viewpoint_guid";

    private static DateFormat formatter = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSZ");
    private static DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
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
    private Date mDate;
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

    private String mViewpiontGuid;

    public Comment(JSONObject obj) {
        construct(obj);
    }

    public Comment(String comment) {
        mComment = comment;
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
            if (obj.has(DATE)) {
                mDate = DateMapper.toDate(obj.getString(DATE));
            }
            if (obj.has(AUTHOR))
                mAuthor = obj.getString(AUTHOR);
            if (obj.has(COMMENT))
                mComment = obj.getString(COMMENT);
            if (obj.has(MODIFIED_DATE))
                mModifiedDate = obj.getString(MODIFIED_DATE);
            if (obj.has(MODIFIED_AUTHOR))
                mModifiedAuthor = obj.getString(MODIFIED_AUTHOR);
            if(obj.has(VIEWPOINT_GUID))
                mViewpiontGuid = obj.getString(VIEWPOINT_GUID);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public String getDate() {
        return DateMapper.map(mDate);
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getComment() {
        return mComment;
    }

    public String getViewpointGuid(){
        return mViewpiontGuid;
    }
    public void setViewpointGuid(String guid){
        mViewpiontGuid = guid;
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
            if(mComment != null)
                map.put(COMMENT, mComment);
            if(mViewpiontGuid != null)
                map.put(VIEWPOINT_GUID, mViewpiontGuid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

    public String toString() {
        return mComment;
    }

}
