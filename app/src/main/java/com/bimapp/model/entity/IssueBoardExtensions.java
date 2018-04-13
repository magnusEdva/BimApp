package com.bimapp.model.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Model implementation of the issue board extensions used by bimsync.
 */
public class IssueBoardExtensions {

    public static final String TOPIC_TYPE = "topic_type";
    public static final String TOPIC_STATUS = "topic_status";
    public static final String USERIDTYPE = "user_id_type";

    private ArrayList<String> mTopicType;
    private ArrayList<String> mTopicStatus;
    private ArrayList<String> mUserIdType;

    public IssueBoardExtensions(JSONObject issueBoardExtensions){
        construct(issueBoardExtensions);
    }

    private void construct(JSONObject issueBoardExtensions) {
        try {
            JSONArray type_array = issueBoardExtensions.getJSONArray(TOPIC_TYPE);
            for (int i =0; i < type_array.length(); i++){

            }

        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public IssueBoardExtensions(ArrayList<String> topicType,ArrayList<String> topicStatus, ArrayList<String> userIdType){
        mTopicType = topicType;
        mTopicStatus = topicStatus;
        mUserIdType = userIdType;
    }

}
