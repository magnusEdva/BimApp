package com.bimapp.model.data_access;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.model.data_access.network.APICall;
import com.bimapp.model.data_access.network.Callback;
import com.bimapp.model.data_access.network.NetworkConnManager;
import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.EntityListConstructor;
import com.bimapp.model.entity.Project;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.entity.Viewpoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    // Sets wheter to download Viewpoints when syncing or not
    private static final boolean SYNC_VIEWPOINTS = false;    // Global variables
    // Define a variable to contain a content resolver instance
    private final ContentResolver mContentResolver;

    // Should eventually move OAuth into the account manager?
    private final AccountManager mAccountManager;

    private final BimApp mContext;


    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(context);
        mContext = (BimApp) context;
        Log.d("SyncAdapter", "Created SyncAdapter") ;
    }


    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(context);
        mContext = (BimApp) context;
        Log.d("SyncAdapter", "Created SyncAdapter, Compat ");
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              SyncResult syncResult) {

        // For debugging the service. Attach debugger to this process (bimapp:sync)
        //android.os.Debug.waitForDebugger();
        Log.d("SyncAdapter", "Started syncing");

        // This posts un-synced new Topics to the server
        PostTopics();
        // This post un-synced updated Topics to the server
        PutTopics();
        // Posts un-synced new Comments to the server
        PostComments();
        // Puts un-synced updated Comments to the server. We don't have those
        PutComments();
        // This gets projects from the server as well as Topics, Comments and ViewPoints
        //GetProjects();

    }

    private void PutComments() {
        UpdateComments(AppDatabase.statusTypes.updated.status);
    }

    private void PostComments() {
        UpdateComments(AppDatabase.statusTypes.New.status);
    }

    private void UpdateComments(String newOrUpdatedRows){


        // Find un-synced comments
        Cursor commentCursor = mContentResolver.query(DataProvider.ParseUri(DataProvider.COMMENT_TABLE),
                null,
                newOrUpdatedRows,
                new String[]{ (DataProvider.LOCAL_ROWS)},
                null);

        List<Comment> comments = new ArrayList<>();
        if (commentCursor != null){
            Log.d("SyncAdapter", "Found " + commentCursor.getCount() + " comments to push to server.");
            if (commentCursor.getCount() !=0 ){
                // If there are new/updated comments, create new objects
                while (commentCursor.moveToNext()) {
                    String commentGuid = commentCursor.getString(commentCursor.getColumnIndex(Comment.GUID));
                    String verbalStatus = commentCursor.getString(commentCursor.getColumnIndex(Comment.VERBAL_STATUS));
                    String status = commentCursor.getString(commentCursor.getColumnIndex(Comment.STATUS));
                    String Date = commentCursor.getString(commentCursor.getColumnIndex(Comment.DATE));
                    String author = commentCursor.getString(commentCursor.getColumnIndex(Comment.AUTHOR));
                    String topicGUID = commentCursor.getString(commentCursor.getColumnIndex(Comment.TOPIC_GUID));
                    String modifiedDate = commentCursor.getString(commentCursor.getColumnIndex(Comment.MODIFIED_DATE));
                    String modifiedAuthor = commentCursor.getString(commentCursor.getColumnIndex(Comment.MODIFIED_AUTHOR));
                    String comment_content = commentCursor.getString(commentCursor.getColumnIndex(Comment.COMMENT));
                    String viewpointGuid = commentCursor.getString(commentCursor.getColumnIndex(Comment.VIEWPOINT_GUID));
                    Long dateAcquired = commentCursor.getLong(commentCursor.getColumnIndex(AppDatabase.DATE_COLUMN));
                    AppDatabase.statusTypes localStatus = AppDatabase.convertStringToStatus
                            (commentCursor.getString(commentCursor.getColumnIndex(AppDatabase.STATUS_COLUMN)));
                    Comment comment = (new Comment(
                            commentGuid,
                            verbalStatus,
                            status,
                            Date,
                            author,
                            topicGUID,
                            modifiedDate,
                            modifiedAuthor,
                            comment_content,
                            viewpointGuid,
                            dateAcquired,
                            localStatus));

                    String projectID = "";

                 //   Log.d("Topic ID" , topicGUID);
                    // Get the Topic so you can get the ProjectID for current comment
                    Cursor topicCursor = mContentResolver.query(
                            DataProvider.ParseUri(DataProvider.TOPIC_TABLE),
                            null,
                            topicGUID,
                            new String[]{DataProvider.GUID},
                            null
                    );
                    if (topicCursor != null){
                        while (topicCursor.moveToNext()) {
                            if (topicCursor.getCount() != 0) {
                                //Log.d("Cursor", DatabaseUtils.dumpCursorToString(topicCursor));
                                projectID = topicCursor.getString(topicCursor.getColumnIndex(Project.PROJECT_ID));
                                //Log.d("SyncAdapter", "Project ID = " + projectID);
                            }
                        }
                        topicCursor.close();
                    }
                    if (viewpointGuid != null) {
                        // If the comment has a ViewPoint, it needs to be posted BEFORE the comment is posted
                        Cursor viewPointCursor = mContentResolver.query(
                                DataProvider.ParseUri(DataProvider.VIEWPOINT_TABLE),
                                null,
                                commentGuid,
                                null,
                                null
                        );
                        if (viewPointCursor != null && viewPointCursor.getCount() != 0){
                            // Add Viewpoint to Comment and send ViewPoint to server.
                            // onSuccess should then post the comment
                            if (viewPointCursor.moveToFirst()) {

                                String guid = viewPointCursor.getString(viewPointCursor.getColumnIndex(Viewpoint.GUID));
                                String commentGUID = viewPointCursor.getString(viewPointCursor.getColumnIndex(Viewpoint.COMMENT_GUID));
                                String type = viewPointCursor.getString(viewPointCursor.getColumnIndex("type"));
                                String pictureName = viewPointCursor.getString(viewPointCursor.getColumnIndex("picture_name"));
                                AppDatabase.statusTypes vp_localStatus = AppDatabase.convertStringToStatus
                                        (viewPointCursor.getString(viewPointCursor.getColumnIndex(AppDatabase.STATUS_COLUMN)));
                                Long vp_dateAcquired = viewPointCursor.getLong(viewPointCursor.getColumnIndex(AppDatabase.DATE_COLUMN));
                                Viewpoint vp = new Viewpoint(guid, commentGUID, type, pictureName, vp_dateAcquired, vp_localStatus);
                                Log.d("SyncAdapter", "Comment "+ comment.getMComment());
                                comment.setViewpoint(vp);
                                // Find Topic, and by the topic the ProjectID

                                vp.getSnapshot();
                                // Post the ViewPoint, send the Comment to the method
                                NetworkConnManager.networkRequest(mContext,
                                        Request.Method.POST,
                                        APICall.POSTViewpoints(projectID,topicGUID),
                                        new PostViewpointCallback(projectID,comment),
                                        vp
                                );
                            }
                        } else{
                            // Posts comment to server if no Viewpoint was found.
                            // Could lead to problems?
                            comments.add(comment);
                        }
                        if (viewPointCursor != null)
                            viewPointCursor.close();

                    } else {
                        // Found no ViewPoint, add the Comment to the list of those to be pushed to server
                        comments.add(comment);
                    }
                }
            }
            commentCursor.close();
        }
        if (comments.size() != 0){
            // Post to server
            for (Comment comment: comments){
                // Get the Topic so you can get the ProjectID for current comment
                Cursor topicCursor = mContentResolver.query(
                        DataProvider.ParseUri(DataProvider.TOPIC_TABLE),
                        null,
                        comment.getMTopicGUID(),
                        new String[]{DataProvider.GUID},
                        null
                );
                String projectID = "";
                if (topicCursor != null){
                    if (topicCursor.getCount() != 0){
                        while(topicCursor.moveToNext()){
                            projectID = topicCursor.getString(topicCursor.getColumnIndex(Project.PROJECT_ID));
                        }
                    }
                    topicCursor.close();
                }
                //Log.d("SyncAdapter", "Project ID = " + projectID);
                NetworkConnManager.networkRequest(
                        mContext,
                        Request.Method.POST,
                        APICall.POSTComment(projectID, comment.getMTopicGUID()),
                        new PostCommentCallback(comment,
                                comment.getMTopicGUID()
                        ),
                        comment
                );
            }
        }
    }

    private void PostTopics(){
        UpdateTopics(AppDatabase.statusTypes.New.status);
    }

    private void PutTopics() {
        UpdateTopics(AppDatabase.statusTypes.updated.status);
    }

    /**
     * This POST or PUT Topics from the Database to the server depending on the @param provided
     * @param newRowsOrUpdatedRows if this is NEW_ROWS, posts those, if this is UPDATED_ROWS, puts those
     */
    private void UpdateTopics(String newRowsOrUpdatedRows) {
        // Find un-synced topics
        Cursor cursor = mContentResolver.query(DataProvider.ParseUri(DataProvider.TOPIC_TABLE),
                null,
                newRowsOrUpdatedRows,
                new String[]{DataProvider.LOCAL_ROWS},
                null);

        if (cursor != null && cursor.getCount() != 0){
            Log.d("SyncAdapter", "Found " + cursor.getCount() + " new posts");
            List<Topic> topics = new ArrayList<>();
            while (cursor.moveToNext()) {
                String guid = cursor.getString(cursor.getColumnIndex(Topic.GUID));
                String description = cursor.getString(cursor.getColumnIndex(Topic.DESCRIPTION));
                int index = cursor.getInt(cursor.getColumnIndex(Topic.INDEX));
                String title = cursor.getString(cursor.getColumnIndex(Topic.TITLE));
                String dueDate = cursor.getString(cursor.getColumnIndex(Topic.DUE_DATE));
                String TopicType = cursor.getString(cursor.getColumnIndex(Topic.TOPIC_TYPE));
                String Status = cursor.getString(cursor.getColumnIndex(Topic.TOPIC_STATUS));
                List<String> Labels = Topic.getListFromString(cursor.getString(cursor.getColumnIndex(Topic.LABELS)));
                List<String> references = Topic.getListFromString(cursor.getString(cursor.getColumnIndex(Topic.REFERENCE_LINKS)));
                String modAuth = cursor.getString(cursor.getColumnIndex(Topic.MODIFIED_AUTHOR));
                String Stage = cursor.getString(cursor.getColumnIndex(Topic.STAGE));

                String mSnippet_type = cursor.getString(cursor.getColumnIndex(Topic.BimSnippet.SNIPPET_TYPE));

                String mReference = cursor.getString(cursor.getColumnIndex(Topic.BimSnippet.REFERENCE));

                String mReferenceSchema = cursor.getString(cursor.getColumnIndex(Topic.BimSnippet.REFERENCE_SCHEMA));

                boolean isExternal = cursor.getInt(cursor.getColumnIndex(Topic.BimSnippet.IS_EXTERNAL)) != 0;

                Topic.BimSnippet snippet = new Topic.BimSnippet(mSnippet_type, mReference, mReferenceSchema, isExternal);

                String priority = cursor.getString(cursor.getColumnIndex(Topic.PRIORITY));
                String creationAuthor = cursor.getString(cursor.getColumnIndex(Topic.CREATION_AUTHOR));
                String CreationDate = cursor.getString(cursor.getColumnIndex(Topic.CREATION_DATE));
                String AssignedTo = cursor.getString(cursor.getColumnIndex(Topic.ASSIGNED_TO));
                String projectId = cursor.getString(cursor.getColumnIndex(Project.PROJECT_ID));

                String statusColumn = cursor.getString(cursor.getColumnIndex(AppDatabase.DATE_COLUMN));
                Long dateAcquired = cursor.getLong(cursor.getColumnIndex(AppDatabase.STATUS_COLUMN));

                Topic topic = new Topic();
                topic.setTitle(title);
                topic.setTopicType(TopicType);
                topic.setTopicStatus(Status);
                topic.setAssignedTo(AssignedTo);
                topic.setDescription(description);
                topic.setBimSnippet(snippet);
                topic.setCreationAuthor(creationAuthor);
                topic.setGuid(guid);
                topic.setLabels(Labels);
                topic.setIndex(index);
                topic.setReferenceLinks(references);
                topic.setDueDate(dueDate);
                topic.setModifiedAuthor(modAuth);
                topic.setStage(Stage);
                topic.setPriority(priority);
                topic.setCreationDate(CreationDate);
                topic.setProjectId(projectId);
                topic.setDateAcquired(dateAcquired);
                topic.setLocalStatus(AppDatabase.convertStringToStatus(statusColumn));
                topics.add(topic);
            }
            cursor.close();
            if (newRowsOrUpdatedRows.equals(AppDatabase.statusTypes.New.status)) {
                for (Topic topic : topics) {
                    NetworkConnManager.networkRequest(mContext, Request.Method.POST,
                            APICall.POSTTopics(topic.getProjectId()), new PostTopicCallback(topic), topic);
                }
            } else if (newRowsOrUpdatedRows.equals(AppDatabase.statusTypes.updated.status)){
                for (Topic topic: topics){
                    NetworkConnManager.networkRequest(mContext, Request.Method.PUT,
                            APICall.PUTTopic(topic.getProjectId(), topic),new PutTopicCallback(topic), topic);
                }
            }
        }
        else
            Log.d("SyncAdapter", "Found no new Topics");
    }

    /**
     * Method to get projects (IssueBoards) from the server for CurrentUser
     *
     * Callback from the result of this methods call should implement getting Topics from said
     * IssueBoard
     */
    private void GetProjects() {
        NetworkConnManager.networkRequest(mContext,
                Request.Method.GET,
                APICall.GETProjects(),
                new ProjectCallback()
                , null);
    }

    /**
     * Method to get Topics from the server for current Project
     * @param project The project you want the topics for
     */
    private void GetTopics(Project project) {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET,
                APICall.GETTopics(project),
                new TopicsCallback(project), null);
    }

    /**
     * Method to get Comments from the server for current Project and Topic
     *
     * Note: Topics do not belong to Projects by default. This is something bimsync does
     *
     * @param mProject The project the comment belongs to
     * @param topic The topic the comment belongs to
     */
    private void GetComments(Project mProject, Topic topic) {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET,
                APICall.GETComments(mProject, topic),
                new CommentsCallback(mProject, topic), null);
    }

    /**
     * Gets the ViewPoint from the server for current Project and Topic
     *
     * @param project ViewPoints belong to this project
     * @param comment The Comment the ViewPoint should be linked to
     */
    private void GetViewPoint(Project project, Comment comment) {
        if (SYNC_VIEWPOINTS) {
            // Should check if this ViewPoint is already in the Database
            Cursor cursor = mContentResolver.query(
                    DataProvider.ParseUri(DataProvider.VIEWPOINT_TABLE),
                    null,
                    comment.getMCommentsGUID(),
                    null,
                    null);
            if (cursor == null || cursor.getCount() == 0) {
                // If ViewPoint is not found on the Database, get it from server
                NetworkConnManager.networkRequest(mContext, Request.Method.GET,
                        APICall.GETViewpoint(project, comment.getMTopicGuid(), comment),
                        new ViewPointCallback(project, comment), null);
            } else {
                // if ViewPoint IS found on the server, set ViewPoint to the Comment
                if (cursor.moveToFirst()) {
                    String guid = cursor.getString(cursor.getColumnIndex(Viewpoint.GUID));
                    String commentGUID = cursor.getString(cursor.getColumnIndex(Viewpoint.COMMENT_GUID));
                    String type = cursor.getString(cursor.getColumnIndex("type"));
                    String pictureName = cursor.getString(cursor.getColumnIndex("picture_name"));
                    AppDatabase.statusTypes localStatus = AppDatabase.convertStringToStatus
                            (cursor.getString(cursor.getColumnIndex(AppDatabase.STATUS_COLUMN)));
                    Long dateAcquired = cursor.getLong(cursor.getColumnIndex(AppDatabase.STATUS_COLUMN));
                    Viewpoint vp = new Viewpoint(guid, commentGUID, type, pictureName, dateAcquired, localStatus);
                    comment.setViewpoint(vp);
                    mContentResolver.insert(DataProvider.ParseUri(DataProvider.VIEWPOINT_TABLE),
                            vp.getContentValues());
                    mContentResolver.insert(DataProvider.ParseUri(DataProvider.COMMENT_TABLE),
                            comment.getContentValues());
                    Log.d("SyncAdapter", "Added ViewPoint from database!");
                    // Log.d("SyncAdapter", "Added Comment " + comment.getMComment());
                }
            }
            if (cursor != null)
                cursor.close();
        }else {
            // Still needs to insert the comment into the database even if the Viewpoint is not
            mContentResolver.insert(DataProvider.ParseUri(DataProvider.COMMENT_TABLE),
                    comment.getContentValues());
        }
    }

    // Private classes implements Volley Callbacks for specific API-callbacks

    /**
     * Class which handles the callback from network with Projects (IssueBoards)
     * Should add received projects to database and call methods to get Topics for all projects
     */
    private class ProjectCallback implements Callback<String>{

        @Override
        public void onError(String response) {
            // TODO Error handling
            Log.d("SyncAdapter", "Error on project callback. " + response);
        }

        @Override
        public void onSuccess(String response) {
            List<Project> projects = null;
            Log.d("SyncAdapter", "Got successful response");
            try {
                JSONArray jsonArray = new JSONArray(response);
                projects = EntityListConstructor.Projects(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (projects != null){
                for (Project project: projects){
                    mContentResolver.insert(DataProvider.ParseUri(DataProvider.PROJECT_TABLE),project.getContentValues());
                    Log.d("SyncAdapter", "Added project " + project.getName() + " to database.");
                    GetTopics(project);
                }

            }
        }
    }
    /**
     * Class which handles the callback from network with Topics
     * Should add received Topics to database and call methods to get Comments for all Topics
     */
    private class TopicsCallback implements Callback<String>{
        private Project mProject;

        private TopicsCallback(Project project) {
            mProject = project;
        }

        @Override
        public void onError(String response) {
            Log.d("SyncAdapter", "Error on topic callback. " + response);
        }

        @Override
        public void onSuccess(String response) {
            List<Topic> topics = null;
            try {
                JSONArray jsonArray = new JSONArray(response);
                topics = EntityListConstructor.Topics(jsonArray,mProject.getProjectId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (topics != null) {
                for (Topic topic : topics) {
                    topic.setProjectId(mProject.getProjectId());
                    mContentResolver.insert(DataProvider.ParseUri(DataProvider.TOPIC_TABLE), topic.getValues());
                    Log.d("SyncAdapter", "Added topic " + topic.getMTitle() + " to database");
                    GetComments(mProject, topic);
                }
            }
        }
    }
    /**
     * Class which handles the callback from network with Comments
     * Should call methods for getting ViewPoints if the comment has a ViewPoint, should then
     * add Comments to topics
     */
    private class CommentsCallback implements Callback<String>{

        private Topic mTopic;
        private Project mProject;

        private CommentsCallback(Project project, Topic topic) {
            mTopic = topic;
            mProject = project;
        }

        @Override
        public void onError(String response) {
            Log.d("SyncAdapter", "Error on comment callback. " + response);
        }

        @Override
        public void onSuccess(String response) {
            List<Comment> comments = null;
            try {
                JSONArray jsonArray = new JSONArray(response);
                comments = EntityListConstructor.Comments(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (comments != null){
                // Check if comment has ViewPoint
                for (Comment comment : comments) {
                    if (comment.getMViewpointGuid() != null) {
                        // If it has ViewPoint, get ViewPoint
                        comment.setTopicGUID(mTopic.getMGuid());
                        GetViewPoint(mProject, comment);
                    } else{
                        // Otherwise add comment to DB
                        comment.setTopicGUID(mTopic.getMGuid());
                        mContentResolver.insert(DataProvider.ParseUri(DataProvider.COMMENT_TABLE),
                                comment.getContentValues());
                    }
                }
            }
        }
    }
    /**
     * Class which handles the callback from network with ViewPoints
     * Should call methods for getting Snapshots if the ViewPoint has a Snapshot, should then
     * post ViewPoint and Comment to Topic. Callbacks should add to Database
     */
    private class ViewPointCallback implements Callback<String> {
        Comment mComment;
        String mProjectID;

        private ViewPointCallback(Project project, Comment comment){
            mComment = comment;
            mProjectID = project.getProjectId();
        }

        private ViewPointCallback(String projectID, Comment comment){
            mComment = comment;
            mProjectID = projectID;
        }

        @Override
        public void onError(String response) {
            Log.d("SyncAdapter", "Error on ViewPoint callback. " + response);
        }

        @Override
        public void onSuccess(String response) {
            Log.d("SyncAdapter", "Successfully posted Viewpoint");
            Viewpoint vp = null;
            try {
                JSONObject jsonObject = new JSONObject(response);
                vp = new Viewpoint(jsonObject, mComment.getMCommentsGUID());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Should always have a ViewPoint if you got here
            if (vp != null) {
                // If ViewPoint has Snapshot, get it
                if (vp.hasSnapshot()) {
                    NetworkConnManager.networkRequest(mContext, 11,
                            APICall.GETSnapshot(mProjectID, mComment.getMTopicGuid(), vp),
                            new SnapshotCallback(mComment,vp), null);
                }
                // Otherwise add Comment and ViewPoint to DB
                else {
                    vp.setCommentGUID(mComment.getMCommentsGUID());
                    mComment.setViewpoint(vp);
                    mContentResolver.insert(DataProvider.ParseUri(DataProvider.VIEWPOINT_TABLE),
                            vp.getContentValues());
                    mContentResolver.insert(DataProvider.ParseUri(DataProvider.COMMENT_TABLE),
                            mComment.getContentValues());
                    Log.d("SyncAdapter", "Added ViewPoint");
                    Log.d("SyncAdapter", "Added Comment " + mComment.getMComment());
                }
            }
        }
    }
    /**
     * Class which handles the callback from network with Snapshots
     * Should add Snapshot, ViewPoint and Comment to topics
     */
    private class SnapshotCallback implements Callback<Bitmap>{
        private Comment mComment;
        private Viewpoint mViewpoint;

        private SnapshotCallback(Comment comment, Viewpoint viewpoint){
            mComment = comment;
            mViewpoint = viewpoint;
        }

        @Override
        public void onError(String response) {
            Log.d("SyncAdapter", "Error on Snapshot callback. " + response);
        }

        @Override
        public void onSuccess(Bitmap response) {
            mViewpoint.constructSnapshot(response);
            mViewpoint.setCommentGUID(mComment.getMCommentsGUID());
            mComment.setViewpoint(mViewpoint);
            mContentResolver.insert(DataProvider.ParseUri(DataProvider.VIEWPOINT_TABLE),
                    mViewpoint.getContentValues());
            Log.d("SyncAdapter", "Added ViewPoint");
            mContentResolver.insert(DataProvider.ParseUri(DataProvider.COMMENT_TABLE),
                    mComment.getContentValues());

            Log.d("SyncAdapter", "Added Comment " + mComment.getMComment());
        }
    }

    /**
     * Class which handles the callback from network when un-posted topics gets pushed to server
     * Should update server with new status of topic!
     */
    private class PostTopicCallback implements Callback<String> {
        Topic mLocalTopic;

        private PostTopicCallback(Topic localTopic){
            mLocalTopic = localTopic;
        }

        @Override
        public void onError(String response) {
            Log.d("SyncAdapter", "Error on posting topic");
        }

        @Override
        public void onSuccess(String JSONResponse) {
            // Should update DB on success
            JSONObject object = null;
            try {
                object = new JSONObject(JSONResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (object != null) {
                // Deletes the old entry from DB
                mContentResolver.delete(DataProvider.ParseUri(DataProvider.TOPIC_TABLE), mLocalTopic.getMGuid(), null);
                Topic serverTopic = new Topic(object, mLocalTopic.getProjectId());
                // Creates a new entry in DB with updated IDs
                mContentResolver.insert(DataProvider.ParseUri(DataProvider.TOPIC_TABLE), serverTopic.getValues());
                Log.d("SyncAdapterPost", "Successfully posted offline topic " + serverTopic.getMTitle() + " to server");

                // Get all Comments belonging to this topic, if any, and post them with the new GUID
                // Needs to take into account that Comments can have ViewPoints
                Cursor commentsCursor = mContentResolver.query(
                        DataProvider.ParseUri(DataProvider.COMMENT_TABLE),
                        null,
                        mLocalTopic.getMGuid(), // Gets local Comments from the OLD topicGUID
                        new String[]{DataProvider.LOCAL_ROWS, AppDatabase.statusTypes.New.status},
                        null);
                if (commentsCursor != null){
                    Log.d("SyncAdapter", "Found " + commentsCursor.getCount() + " new comments belonging to topic \"" + mLocalTopic.getMTitle() + "\"");
                    if (commentsCursor.getCount() != 0){
                        while (commentsCursor.moveToNext()) {

                            String guid = commentsCursor.getString(commentsCursor.getColumnIndex(Comment.GUID));
                            String verbalStatus = commentsCursor.getString(commentsCursor.getColumnIndex(Comment.VERBAL_STATUS));
                            String status = commentsCursor.getString(commentsCursor.getColumnIndex(Comment.STATUS));
                            String Date = commentsCursor.getString(commentsCursor.getColumnIndex(Comment.DATE));
                            String author = commentsCursor.getString(commentsCursor.getColumnIndex(Comment.AUTHOR));
                            String topic = commentsCursor.getString(commentsCursor.getColumnIndex(Comment.TOPIC_GUID));
                            String modifiedDate = commentsCursor.getString(commentsCursor.getColumnIndex(Comment.MODIFIED_DATE));
                            String modifiedAuthor = commentsCursor.getString(commentsCursor.getColumnIndex(Comment.MODIFIED_AUTHOR));
                            String comment_content = commentsCursor.getString(commentsCursor.getColumnIndex(Comment.COMMENT));
                            String viewpointGuid = commentsCursor.getString(commentsCursor.getColumnIndex(Comment.VIEWPOINT_GUID));
                            Long dateAcquired = commentsCursor.getLong(commentsCursor.getColumnIndex(AppDatabase.DATE_COLUMN));
                            AppDatabase.statusTypes localStatus = AppDatabase.convertStringToStatus
                                    (commentsCursor.getString(commentsCursor.getColumnIndex(AppDatabase.STATUS_COLUMN)));
                            Comment comment = new Comment(guid, verbalStatus, status, Date, author, topic,
                                    modifiedDate, modifiedAuthor, comment_content, viewpointGuid, dateAcquired, localStatus);
                            // Method responsible for posting a single comment, with potential
                            // Viewpoint, to the server
                            PostComment(mLocalTopic, serverTopic, comment);
                        }
                    }
                    commentsCursor.close();
                }
            }
        }
    }

    /**
     * Method responsible for posting a single comment, with a potential Viewpoint, to the server
     *
     * Should delete the old comment from server on successful response
     * @param localTopic the local topic, needed to find the comment locally
     * @param serverTopic the topic received from the server, needed to link the comment
     * @param comment the comment that should be posted
     */
    private void PostComment(Topic localTopic, Topic serverTopic, Comment comment) {
        Log.d("SyncAdapter", "Tried to post new comment from new topic");
        if (comment.getMCommentsGUID() != null){
            // Get the Viewpoint from database
            Cursor vpCursor = mContentResolver.query(
                    DataProvider.ParseUri(DataProvider.VIEWPOINT_TABLE),
                    null,
                    comment.getMCommentsGUID(),
                    null,
                    null
            );
            if (vpCursor != null){
                Log.d("SyncAdapter", "Got " + vpCursor.getCount() + " viewpoints");
                if (vpCursor.getCount() != 0){
                    while (vpCursor.moveToNext()){
                        String guid = vpCursor.getString(vpCursor.getColumnIndex(Viewpoint.GUID));
                        String commentGUID = vpCursor.getString(vpCursor.getColumnIndex(Viewpoint.COMMENT_GUID));
                        String type = vpCursor.getString(vpCursor.getColumnIndex("type"));
                        String pictureName = vpCursor.getString(vpCursor.getColumnIndex("picture_name"));
                        AppDatabase.statusTypes localStatus = AppDatabase.convertStringToStatus
                                (vpCursor.getString(vpCursor.getColumnIndex(AppDatabase.STATUS_COLUMN)));
                        Long dateAcquired = vpCursor.getLong(vpCursor.getColumnIndex(AppDatabase.DATE_COLUMN));
                        Viewpoint vp = new Viewpoint(guid, commentGUID, type, pictureName, dateAcquired, localStatus);
                        vp.getSnapshot();
                        comment.setViewpoint(vp);
                    }
                }
                vpCursor.close();
            }

            NetworkConnManager.networkRequest(
                    mContext,
                    Request.Method.POST,
                    APICall.POSTViewpoints(serverTopic.getProjectId(), serverTopic.getMGuid()),
                    new PostViewpointCallback(localTopic, serverTopic,comment, comment.getMViewpoint()),
                    comment.getMViewpoint()
            );

        } else {
            // Posts the comment if there is no ViewPoint
            NetworkConnManager.networkRequest(
                    mContext,
                    Request.Method.POST,
                    APICall.POSTComment(serverTopic.getProjectId(), serverTopic.getMGuid()),
                    new PostCommentCallback(comment,localTopic,serverTopic,null),
                    comment
            );
        }
    }

    /**
     * Class which handles the callback from network when un-posted topics gets pushed to server
     * Should delete local version of topic and add response to DB
     */
    private class PutTopicCallback implements Callback<String>{
        Topic mTopic;

        private PutTopicCallback(Topic topic){
            mTopic = topic;
        }


        @Override
        public void onError(String response) {
            Log.d("SyncAdapter", "Error on updating topic");
        }

        @Override
        public void onSuccess(String JSONResponse) {
            // Should update DB on success
            JSONObject object = null;
            try {
                object = new JSONObject(JSONResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (object != null) {
                // Deletes the old entry from DB
                mContentResolver.delete(DataProvider.ParseUri(DataProvider.TOPIC_TABLE), mTopic.getMGuid(), null);
                Topic topic = new Topic(object, mTopic.getProjectId());
                // Creates a new entry in DB with updated IDs
                mContentResolver.insert(DataProvider.ParseUri(DataProvider.TOPIC_TABLE), topic.getValues());
                Log.d("SyncAdapterPost", "Successfully put offline topic " + topic.getMTitle() + " to server");
            }
        }
    }

    /**
     * Class which handles the callback from network when un-posted Comments gets pushed to server
     * Should delete old comment and add new comment to topic
     */
    private class PostCommentCallback implements Callback<String>{
        private Comment mComment;
        private Topic mLocalTopic;
        private String mServerTopic;
        private Viewpoint mViewpoint;

        /**
         * Constructor for posting Comments with or without Viewpoints from a Topic created offline.
         *
         * @param comment The comment to be posted
         * @param localTopic the local topic, should be deleted from DB after
         * @param serverTopic the TopicGUID from the server
         * @param viewpoint the Viewpoint that is to be posted, can be null
         */
        private PostCommentCallback(Comment comment, Topic localTopic, Topic serverTopic, Viewpoint viewpoint) {
            mComment = comment;
            mLocalTopic = localTopic;
            mViewpoint = viewpoint;
            mServerTopic = serverTopic.getMGuid();
        }

        /**
         * Constructor for posting Comments whose topic was already on the server
         * @param comment the comment to be posted
         * @param topicGUID the TopicGUID of the server topic
         */
        private PostCommentCallback(Comment comment, String topicGUID) {
            mComment = comment;
            mServerTopic = topicGUID;
            mViewpoint = null;
        }

        /**
         * Constructor used to post comments after a Viewpoint has been posted.
         * Must delete the Viewpoint and the Comment from the DB
         * @param comment the comment to be posted
         * @param vp the viewpoint that should be deleted
         */
        private PostCommentCallback(Comment comment, Viewpoint vp) {
            mComment = comment;
            mViewpoint = vp;
        }


        @Override
        public void onError(String response) {
            Log.d("SyncAdapter", "Error on posting comment " + response);
        }

        @Override
        public void onSuccess(String response) {
            // TODO Make new Comment from response, get ViewPoint if it has, then delete from
            // Make new Comment from response, attach to new topic, delete old Comment from DB and add new Comment to DB
            Comment comment = null;
            try {
                JSONObject jsonObject = new JSONObject(response);
                comment = new Comment(jsonObject);
                comment.setTopicGUID(mServerTopic);
                if (mViewpoint != null){
                    comment.setViewpoint(mViewpoint);
                    // Overwrites Viewpoint, now has correct CommentGUID
                    mContentResolver.insert(DataProvider.ParseUri(DataProvider.VIEWPOINT_TABLE),
                            mViewpoint.getContentValues()
                    );
                }
                Log.d("SyncAdapter", "Posted Comment to server ");
                mContentResolver.insert(DataProvider.ParseUri(DataProvider.COMMENT_TABLE),
                        comment.getContentValues());
                mContentResolver.delete(DataProvider.ParseUri(DataProvider.COMMENT_TABLE),
                        mComment.getMCommentsGUID(),
                        null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Class which handles the callback from network on a POST Viewpoint request
     *
     * Must post the associated Comment as well
     */
    private class PostViewpointCallback implements Callback<String> {
        Topic mLocalTopic;
        Topic mServerTopic;
        Comment mComment;
        Viewpoint mViewpoint;
        String mProjectID;
        String mServerTopicID;

        private PostViewpointCallback(Topic localTopic, Topic serverTopic, Comment comment, Viewpoint viewpoint) {
            mLocalTopic = localTopic;
            mServerTopic = serverTopic;
            mComment = comment;
            mViewpoint = viewpoint;
            mProjectID = mServerTopic.getProjectId();
            mServerTopicID = mServerTopic.getMGuid();
        }

        private PostViewpointCallback(String projectID, Comment comment) {
            mComment = comment;
            mProjectID = projectID;
            mServerTopicID = comment.getMTopicGUID();
        }

        @Override
        public void onError(String response) {
            Log.d("SyncAdapter", "Error on posting Viewpoint" + response);
        }

        @Override
        public void onSuccess(String  response) {
            Log.d("SyncAdapter", "Posted viewpoint to server");
            Viewpoint vp;
            try {
                JSONObject jsonObject = new JSONObject(response);
                vp = new Viewpoint(jsonObject,mComment.getMCommentsGUID());
                // Deletes old Viewpoint and inserts new
                if (mViewpoint != null) {
                    mContentResolver.delete(
                            DataProvider.ParseUri(DataProvider.VIEWPOINT_TABLE),
                            mViewpoint.getMGuid(),
                            null
                    );
                }
                mContentResolver.insert(
                        DataProvider.ParseUri(DataProvider.VIEWPOINT_TABLE),
                        vp.getContentValues()
                );
                if (mLocalTopic != null && mServerTopic != null){
                    mComment.setViewpoint(vp);
                    mComment.setViewpointGuid(vp.getMGuid());
                    mComment.setTopicGUID(mServerTopic.getMGuid());
                    NetworkConnManager.networkRequest(
                            mContext,
                            Request.Method.POST,
                            APICall.POSTComment(mProjectID, mServerTopicID),
                            new PostCommentCallback(mComment, mLocalTopic, mServerTopic, vp),
                            mComment
                    );
                } else{
                    mComment.setViewpoint(vp);
                    mComment.setViewpointGuid(vp.getMGuid());
                    mComment.setTopicGUID(mServerTopicID); // Should already be set
                    NetworkConnManager.networkRequest(
                            mContext,
                            Request.Method.POST,
                            APICall.POSTComment(mProjectID, mServerTopicID),
                            new PostCommentCallback(mComment,vp),
                            mComment
                    );

                }
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}