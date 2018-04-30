package com.bimapp.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Model implementation of the issue board extensions used by bimsync.
 * <p>
 * Constructors are available for default data or data from a JSONObject.
 * <p>
 * If this class is expanded to provide support for more Extensions, the default constructor should
 * be updated to provide default values.
 */
public class IssueBoardExtensions {

    // Variables to make sure typos are less of an issue
    public static final String TOPIC_TYPE = "topic_type";
    public static final String TOPIC_STATUS = "topic_status";
    public static final String USER_ID_TYPE = "user_id_type";
    public static final String TOPIC_LABELS = "topic_label";

    @ColumnInfo(name = TOPIC_TYPE)
    @TypeConverters(Topic.class)
    private List<String> mTopicType;
    @ColumnInfo(name = TOPIC_STATUS)
    @TypeConverters(Topic.class)
    private List<String> mTopicStatus;
    @ColumnInfo(name = USER_ID_TYPE)
    @TypeConverters(Topic.class)
    private List<String> mUserIdType;
    @ColumnInfo(name = TOPIC_LABELS)
    @TypeConverters(Topic.class)
    private List<String> mTopicLabel;

    /**
     * used to store the isuebaord into the database.
     * without having to deconstruct the List back into a JSON string.
     */
    @Ignore
    private JSONObject object;


    /**
     * Constructor which generates extentions based on a JSONObject from the server
     *
     * @param issueBoardExtensions the JSONObject which contains the data
     */
    public IssueBoardExtensions(JSONObject issueBoardExtensions) {
        construct(issueBoardExtensions);
    }

    /**
     * Constructor for manually adding extensions to an issueboard. Used when constructing an object
     * from storage.
     *
     * @param topicType
     * @param topicStatus
     * @param userIdType
     * @param topicLabel
     */
    public IssueBoardExtensions(List<String> topicType, List<String> topicStatus, List<String> userIdType, List<String> topicLabel) {
        mTopicType = topicType;
        mTopicStatus = topicStatus;
        mUserIdType = userIdType;
        mTopicLabel = topicLabel;
    }

    /**
     * Empty constructor to return the default set of extensions.
     */
    @Ignore
    public IssueBoardExtensions() {
        String[] status = {"Open", "Closed"};
        String[] type = {"Information", "Error"};
        mTopicType = new ArrayList<String>(Arrays.asList(type));
        mTopicStatus = new ArrayList<String>(Arrays.asList(status));
        mTopicLabel = new ArrayList<String>();
        mUserIdType = new ArrayList<String>();
    }

    /**
     * Used to return default values when retrieving Extensions from storage
     *
     * @return a default set of status
     */
    public static Set<String> defaultStatus() {
        String[] status = {"Open", "Closed"};
        return new HashSet<>(Arrays.asList(status));
    }

    /**
     * Used to return default values when retrieving Extensions from storage
     *
     * @return a default set of type
     */
    public static Set<String> defaultType() {
        String[] type = {"Information", "Error"};
        return new HashSet<>(Arrays.asList(type));
    }


    /**
     * Populates member variables with data from a JSONObject
     *
     * @param issueBoardExtensions JASONObject which contains the arrays containing the data
     */
    public void construct(JSONObject issueBoardExtensions) {
        object = issueBoardExtensions;
        mTopicType = new ArrayList<>();
        mTopicStatus = new ArrayList<>();
        mUserIdType = new ArrayList<>();
        mTopicLabel = new ArrayList<>();
        try {
            JSONArray type_array = issueBoardExtensions.getJSONArray(TOPIC_TYPE);
            for (int i = 0; i < type_array.length(); i++) {
                mTopicType.add(type_array.getString(i));
            }
            JSONArray status_array = issueBoardExtensions.getJSONArray(TOPIC_STATUS);
            for (int i = 0; i < status_array.length(); i++) {
                mTopicStatus.add(status_array.getString(i));
            }
            JSONArray user_array = issueBoardExtensions.getJSONArray(USER_ID_TYPE);
            for (int i = 0; i < user_array.length(); i++) {
                mUserIdType.add(user_array.getString(i));
            }
            JSONArray label_array = issueBoardExtensions.getJSONArray(TOPIC_LABELS);
            for (int i = 0; i < label_array.length(); i++) {
                mTopicLabel.add(label_array.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return a String representation of this instance in JSON.
     */
    public String deConstruct() {
        if (object != null)
            return object.toString();
        else
            return null;
    }

    public List<String> getTopicType() {
        return mTopicType;
    }

    public List<String> getTopicStatus() {
        return mTopicStatus;
    }

    public List<String> getUserIdType() {
        return mUserIdType;
    }

    public List<String> getTopicLabel() {
        return mTopicLabel;
    }

}