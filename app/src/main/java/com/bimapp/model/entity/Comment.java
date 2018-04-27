package com.bimapp.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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
@Entity(tableName = "comment")
public class Comment implements entity {
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
    @PrimaryKey
    @NonNull
    private String mCommentsGUID;
    /**
     * comment verbal status
     */
    @ColumnInfo(name = VERBAL_STATUS)
    private String mVerbalStatus;
    /**
     * comment status
     */
    @ColumnInfo(name = STATUS)
    private String mStatus;
    /**
     * creation date
     */
    @ColumnInfo(name = DATE)

    private Date mDate;
    /**
     * comment author
     */
    @ColumnInfo(name = AUTHOR)
    private String mAuthor;
    /**
     * topic id
     */
    @ColumnInfo(name = TOPIC_GUID)
    private String mTopicGUID;
    /**
     * modified at
     */
    @ColumnInfo(name = MODIFIED_DATE)
    private String mModifiedDate;
    /**
     * modified by
     */
    @ColumnInfo(name = MODIFIED_AUTHOR)
    private String mModifiedAuthor;
    /**
     * actual comment content
     */
    @ColumnInfo(name = COMMENT)
    private String mComment;

    @ColumnInfo(name = VIEWPOINT_GUID)
    private String mViewpointGuid;

    @Embedded
    private Viewpoint mViewpoint;

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
            if (obj.has(DATE))
                mDate = DateMapper.toDate(obj.getString(DATE));
            if (obj.has(AUTHOR))
                mAuthor = obj.getString(AUTHOR);
            if (obj.has(COMMENT))
                mComment = new String(obj.getString(COMMENT).getBytes("ISO-8859-1"), "UTF-8");
            if (obj.has(MODIFIED_DATE))
                mModifiedDate = obj.getString(MODIFIED_DATE);
            if (obj.has(MODIFIED_AUTHOR))
                mModifiedAuthor = obj.getString(MODIFIED_AUTHOR);
            if(obj.has(VIEWPOINT_GUID))
                mViewpointGuid = obj.getString(VIEWPOINT_GUID);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public JSONObject getJsonParams() {
        JSONObject map = new JSONObject();
        try {
            if(mComment != null)
                map.put(COMMENT, mComment);
            if(mViewpointGuid != null)
                map.put(VIEWPOINT_GUID, mViewpointGuid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }
    @Override
    public String toString() {
        return mComment;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) {
            return false;
        }
        if (!Comment.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Comment other = (Comment) obj;
        if(mCommentsGUID == null)
            return false;
        return mCommentsGUID.equals(other.mCommentsGUID);
    }

    public String getDate() {
        return DateMapper.map(mDate);
    }

    public Date getMDate(){return mDate;}

    public String getMAuthor() {
        return mAuthor;
    }

    public String getMComment() {
        return mComment;
    }

    public String getMTopicGuid(){
        return mTopicGUID;
    }

    public String getMViewpointGuid(){
        return mViewpointGuid;
    }

    public Viewpoint getMViewpoint(){
        return mViewpoint;
    }


    public String getMCommentsGUID() {
        return mCommentsGUID;
    }

    public void setCommentsGUID(String mCommentsGUID) {
        this.mCommentsGUID = mCommentsGUID;
    }

    public String getMVerbalStatus() {
        return mVerbalStatus;
    }

    public void setVerbalStatus(String mVerbalStatus) {
        this.mVerbalStatus = mVerbalStatus;
    }

    public String getMStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }


    public void setDate(Date mDate) {
        this.mDate = mDate;
    }


    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getMTopicGUID() {
        return mTopicGUID;
    }

    public void setTopicGUID(String mTopicGUID) {
        this.mTopicGUID = mTopicGUID;
    }

    public String getMModifiedDate() {
        return mModifiedDate;
    }

    public void setModifiedDate(String mModifiedDate) {
        this.mModifiedDate = mModifiedDate;
    }

    public String getMModifiedAuthor() {
        return mModifiedAuthor;
    }

    public void setModifiedAuthor(String mModifiedAuthor) {
        this.mModifiedAuthor = mModifiedAuthor;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
    }

    public void setViewpointGuid(String guid){
        mViewpointGuid = guid;
    }

    public void setViewpoint(Viewpoint vp){
        mViewpoint = vp;
    }
}
