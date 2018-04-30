package com.bimapp.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.ContentValues;
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
@Entity(tableName = "topic")
public class Topic implements entity {
    public static final String GUID = "guid";
    public static final String TOPIC_TYPE = "topic_type";
    public static final String TOPIC_STATUS = "topic_status";
    public static final String REFERENCE_LINKS = "reference_links";
    public static final String TITLE = "title";
    public static final String PRIORITY = "priority";
    public static final String INDEX = "index";
    public static final String LABELS = "labels";
    public static final String CREATION_DATE = "creation_date";
    public static final String CREATION_AUTHOR = "creation_author";
    public static final String MODIFIED_DATE = "modified_date";
    public static final String MODIFIED_AUTHOR = "modified_author";
    public static final String ASSIGNED_TO = "assigned_to";
    public static final String STAGE = "stage";
    public static final String DESCRIPTION = "description";
    public static final String BIM_SNIPPET = "bim_snippet";
    public static final String DUE_DATE = "due_date";
    public static final String AUTHORIZATION = "authorization";

    @PrimaryKey
    @ColumnInfo(name = GUID)
    @NonNull
    private String mGuid;

    @ColumnInfo(name = TOPIC_TYPE)
    private String mTopicType;

    @ColumnInfo(name = TOPIC_STATUS)
    private String mTopicStatus;

    @ColumnInfo(name = REFERENCE_LINKS)
    @TypeConverters(Topic.class)
    private List<String> mReferenceLinks;
    /**
     * title of the topic. Used to quickly identify what the topic is about.
     */

    @ColumnInfo(name = TITLE)
    private String mTitle;

    @ColumnInfo(name = PRIORITY)
    private String mPriority;

    @ColumnInfo(name = INDEX)
    private Integer mIndex;

    @ColumnInfo(name = CREATION_DATE)
    private String mCreationDate;

    @ColumnInfo(name = CREATION_AUTHOR)
    private String mCreationAuthor;

    @ColumnInfo(name = MODIFIED_AUTHOR)
    private String mModifiedAuthor;

    @ColumnInfo(name = ASSIGNED_TO)
    private String mAssignedTo;

    @ColumnInfo(name = LABELS)
    @TypeConverters(Topic.class)
    private List<String> mLabels;

    @ColumnInfo(name = STAGE)
    private String mStage;
    /**
     * actual description of the Topic. Used with the title to provide full context.
     */
    @ColumnInfo(name = DESCRIPTION)
    private String mDescription;

    @Embedded
    private BimSnippet mBimSnippet;

    @ColumnInfo(name = DUE_DATE)
    private String mDueDate;

    @ColumnInfo(name = Project.PROJECT_ID)
    private String projectId;

    public Topic(JSONObject obj) {
        construct(obj);
    }

    public Topic(@NonNull String title, @Nullable String topicType, @Nullable String topicStatus,
                 @Nullable String assignedTo, @Nullable String description, @NonNull String projectId) {
        mTitle = title;
        mTopicType = topicType;
        mTopicStatus = topicStatus;
        mAssignedTo = assignedTo;
        mDescription = description;
        this.projectId = projectId;
    }

    public Topic(ContentValues values) {
        mGuid = values.getAsString(GUID);
        mTitle = values.getAsString(TITLE);
        mTopicType = values.getAsString(TOPIC_TYPE);
        mTopicStatus = values.getAsString(TOPIC_STATUS);
        mLabels = getListFromString(values.getAsString(LABELS));
        mReferenceLinks = getListFromString((values.getAsString(REFERENCE_LINKS)));
        mDueDate = values.getAsString(DUE_DATE);
        mPriority = values.getAsString(PRIORITY);
        mDescription = values.getAsString(DESCRIPTION);
        mIndex = values.getAsInteger(INDEX);
        mCreationAuthor = values.getAsString(CREATION_AUTHOR);
        mCreationDate = values.getAsString(CREATION_DATE);
        mAssignedTo = values.getAsString(ASSIGNED_TO);
        mStage = values.getAsString(STAGE);

        projectId = values.getAsString(Project.PROJECT_ID);
        mModifiedAuthor = values.getAsString(MODIFIED_AUTHOR);
        try {
            mBimSnippet = new BimSnippet(new JSONObject(values.getAsString(BIM_SNIPPET)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    @Override
    public JSONObject getJsonParams() {
        JSONObject map = new JSONObject();
        try {
            if (mTopicType != null)
                map.put(TOPIC_TYPE, mTopicType);
            if (mTopicStatus != null)
                map.put(TOPIC_STATUS, mTopicStatus);
            if (mReferenceLinks != null && !mReferenceLinks.isEmpty())
                map.put(REFERENCE_LINKS, getJSONArrayFromList(mReferenceLinks));
            if (mLabels != null && !mLabels.isEmpty())
                map.put(LABELS, getJSONArrayFromList(mLabels));
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

    public ContentValues getValues(){
        ContentValues values = new ContentValues();
        values.put(GUID,mGuid);
        values.put(TOPIC_STATUS, mTopicStatus);
        values.put(REFERENCE_LINKS, getStringFromList(mReferenceLinks));
        values.put(LABELS, getStringFromList(mLabels));
        values.put(TOPIC_TYPE, mTopicType);
        values.put(TITLE, mTitle);
        values.put(PRIORITY,mPriority);
        values.put(INDEX,mIndex);
        values.put(ASSIGNED_TO,mAssignedTo);
        values.put(STAGE, mStage);
        values.put(DESCRIPTION,mDescription);
        values.put(BIM_SNIPPET, mBimSnippet.getJSON().toString());
        values.put(Project.PROJECT_ID, projectId);
        values.put(DUE_DATE,mDueDate);
        values.put(CREATION_AUTHOR, mCreationAuthor);
        values.put(CREATION_DATE, mCreationDate);
        return values;
    }

    public static List<String> getListFromJSonArray(JSONArray array) {
        if(array == null)
            return null;
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
    public static JSONArray getJSONArrayFromList(List<String> list){
        if(list == null)
            return null;
        JSONArray array = new JSONArray();
        for(String s : list){
            array.put(s);
        }
        return array;
    }
    @TypeConverter
    public static String getStringFromList(List<String> list){
        JSONArray arr = getJSONArrayFromList(list);
        if(arr != null)
            return arr.toString();
        else
            return null;
    }
    @TypeConverter
    public static List<String> getListFromString(String string){
        if(string == null)
            return null;
        JSONArray array = null;
        try {
             array = new JSONArray(string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getListFromJSonArray(array);
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public String getMGuid() {
        return mGuid;
    }

    public void setGuid(String guid){mGuid = guid;}

    public String getMTopicType() {
        return mTopicType;
    }

    public void setTopicType(String topicType) {
        mTopicType = topicType;
    }

    public String getMTopicStatus() {
        return mTopicStatus;
    }

    public void setTopicStatus(String topicStatus) {
        mTopicStatus = topicStatus;
    }

    public List<String> getMReferenceLinks() {
        return mReferenceLinks;
    }

    public void setReferenceLinks(List<String> links){mReferenceLinks = links;}

    public String getMTitle() {
        return mTitle;
    }

    public String getMPriority() {
        return mPriority;
    }

    public void setPriority(String priority){mPriority = priority;}

    public Integer getMIndex() {
        return mIndex;
    }

    public void setIndex(Integer index){
        mIndex = index;
    }

    public String getMCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(String creationDate){
        mCreationDate = creationDate;
    }

    public String getMCreationAuthor() {
        return mCreationAuthor;
    }

    public void setCreationAuthor(String author){
        mCreationAuthor = author;
    }

    public String getMModifiedAuthor() {
        return mModifiedAuthor;
    }

    public void setModifiedAuthor(String modifiedAuthor){
        mModifiedAuthor = modifiedAuthor;
    }
    public String getMAssignedTo() {
        return mAssignedTo;
    }

    public String getMStage() {
        return mStage;
    }

    public void setStage(String stage){
        mStage = stage;
    }

    public String getMDescription() {
        return mDescription;
    }

    public String getMDueDate() {
        return mDueDate;
    }

    public void setDueDate(String dueDate){
        mDueDate = dueDate;
    }

    public BimSnippet getMBimSnippet() {
        return mBimSnippet;
    }
    public void setBimSnippet(BimSnippet snippet){
        mBimSnippet = snippet;
    }
    public List<String> getMLabels(){
        return mLabels;
    }
    public void setLabels(List<String> labels){
        mLabels = labels;
    }
    public String getProjectId(){
        return projectId;
    }
    public void setProjectId(String projectId){
        this.projectId = projectId;
    }


    public static class BimSnippet {
        final static String SNIPPET_TYPE = "snippet_type";
        final static String IS_EXTERNAL = "is_external";
        final static String REFERENCE = "reference";
        final static String REFERENCE_SCHEMA = "reference_schema";


        @ColumnInfo(name = SNIPPET_TYPE)
        public String mSnippet_type;

        @ColumnInfo(name = IS_EXTERNAL)
        public boolean mExternal;

        @ColumnInfo(name = REFERENCE)
        public String mReference;

        @ColumnInfo(name = REFERENCE_SCHEMA)
        public String mReferenceSchema;

        public BimSnippet(){}

        public BimSnippet(JSONObject snippet) {
           construct(snippet);
        }

        /**
         * @param string * JSON formatted String
         */
        public BimSnippet(String string) {
            try {
                JSONObject obj = new JSONObject(string);
                construct(obj);
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

        private void construct(JSONObject snippet){
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
    }

}
