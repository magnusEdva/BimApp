package com.bimapp.model.entity;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Model implementation of the issue board extensions used by bimsync.
 */
public class IssueBoardExtensions {

    public static final String TOPIC_TYPE = "topic_type";
    public static final String TOPIC_STATUS = "topic_status";
    public static final String USER_ID_TYPE = "user_id_type";
    public static final String TOPIC_LABELS = "topic_label";

    private ArrayList<String> mTopicType;
    private ArrayList<String> mTopicStatus;
    private ArrayList<String> mUserIdType;
    private ArrayList<String> mTopicLabel;

    /**
     * Constructor which generates extentions based on a JSONObject from the server
     * @param issueBoardExtensions the JSONObject which contains the data
     */
    public IssueBoardExtensions(JSONObject issueBoardExtensions){
        construct(issueBoardExtensions);
    }

    /**
     * Constructor for manually adding extensions to an issueboard. Used when constructing an object
     * from storage.
     * @param topicType
     * @param topicStatus
     * @param userIdType
     * @param topicLabel
     */
    public IssueBoardExtensions(ArrayList<String> topicType,ArrayList<String> topicStatus, ArrayList<String> userIdType, ArrayList<String> topicLabel){
        mTopicType = topicType;
        mTopicStatus = topicStatus;
        mUserIdType = userIdType;
        mTopicLabel = topicLabel;
    }

    /**
     * Empty constructor to return the default set of extensions
     */
    public IssueBoardExtensions(){
        String[] status = {"Open", "Closed"};
        String[] type ={"Information", "Error"};
        mTopicType = new ArrayList<String>(Arrays.asList(type));
        mTopicStatus = new ArrayList<String>(Arrays.asList(status));
        mTopicLabel = new ArrayList<String>();
        mUserIdType = new ArrayList<String>();
    }
    public static Set<String> defaultStatus(){
        String[] status = {"Open", "Closed"};
        return new HashSet<>(Arrays.asList(status));
    }

    public static Set<String> defaultType(){
        String[] type ={"Information", "Error"};
        return new HashSet<>(Arrays.asList(type));
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
                mTopicType.add(type_array.getString(i));
            }
            JSONArray status_array = issueBoardExtensions.getJSONArray(TOPIC_STATUS);
            mTopicStatus = new ArrayList<>();
            for (int i = 0; i < status_array.length();i++){
                mTopicStatus.add(status_array.getString(i));
            }
            JSONArray user_array = issueBoardExtensions.getJSONArray(USER_ID_TYPE);
            mUserIdType = new ArrayList<>();
            for (int i = 0; i < user_array.length();i++){
                mUserIdType.add(user_array.getString(i));
            }
            JSONArray label_array = issueBoardExtensions.getJSONArray(TOPIC_LABELS);
            mTopicLabel = new ArrayList<>();
            for (int i = 0; i < label_array.length();i++){
                mTopicLabel.add(label_array.getString(i));
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

    public ArrayList<String> getTopicLabel() {
        return mTopicLabel;
    }
}