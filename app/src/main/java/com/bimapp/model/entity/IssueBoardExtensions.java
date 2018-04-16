package com.bimapp.model.entity;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Map;

/**
 * Model implementation of the issue board extensions used by bimsync.
 */
public class IssueBoardExtensions {

    public static final String TOPIC_TYPE = "topic_type";
    public static final String TOPIC_STATUS = "topic_status";
    public static final String USER_ID_TYPE = "user_id_type";

    private ArrayList<String> mTopicType;
    private ArrayList<String> mTopicStatus;
    private ArrayList<String> mUserIdType;

    /**
     * Constructor which generates extentions based on a JSONObject from the server
     * @param issueBoardExtensions the JSONObject which contains the data
     */
    public IssueBoardExtensions(JSONObject issueBoardExtensions){
        construct(issueBoardExtensions);
    }

    /**
     * Constructor for manually adding extensions to an issueboard
     * @param topicType
     * @param topicStatus
     * @param userIdType
     */
    public IssueBoardExtensions(ArrayList<String> topicType,ArrayList<String> topicStatus, ArrayList<String> userIdType){
        mTopicType = topicType;
        mTopicStatus = topicStatus;
        mUserIdType = userIdType;
    }


    /**
     * Populates member variables with data from a JSONObject
     * @param issueBoardExtensions JASONObject which contains the arrays containing the data
     */
    public void construct(JSONObject issueBoardExtensions) {
        try {
            JSONArray type_array = issueBoardExtensions.getJSONArray(TOPIC_TYPE);
            mTopicType = new ArrayList<>();
            for (int i =0; i < type_array.length(); i++){
                mTopicType.add(new String (type_array.getString(i)));
            }
            JSONArray status_array = issueBoardExtensions.getJSONArray(TOPIC_STATUS);
            mTopicStatus = new ArrayList<>();
            for (int i = 0; i < status_array.length();i++){
                mTopicStatus.add(new String (status_array.getString(i)));
            }
            JSONArray user_array = issueBoardExtensions.getJSONArray(USER_ID_TYPE);
            mUserIdType = new ArrayList<>();
            for (int i = 0; i < user_array.length();i++){
                mUserIdType.add(new String (user_array.getString(i)));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
    public ArrayList<String> getTopicType() {
        return mTopicType;
    }

    public ArrayList<String> getTopicStatus() {
        return mTopicStatus;
    }

    public ArrayList<String> getUserIdType() {
        return mUserIdType;
    }
}