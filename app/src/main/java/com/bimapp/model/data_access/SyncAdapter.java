package com.bimapp.model.data_access;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
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

import java.util.List;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {    // Global variables
    // Define a variable to contain a content resolver instance
    private final ContentResolver mContentResolver;

    private final AccountManager mAccountManager;

    private final BimApp mContext;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
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
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
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

        Log.d("SyncAdapter", "Started syncing");

        // This gets projects from the server
        GetProjects();

        // This posts un-synced Topics to the server
        PostTopics();

    }

    private void PostTopics() {

        // Post un-synced topics

        // Then if Comment has ViewPoint, post ViewPoints

        // Then post Comment
    }

    /**
     * Method to get projects (IssueBoards) from the server.
     *
     * Callback from the result of this methods call should implement getting Topics from said
     * IssueBoard
     */
    private void GetProjects() {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET, APICall.GETProjects(),new ProjectCallback()
                , null);
    }

    private void GetTopics(Project project) {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET,
                APICall.GETTopics(project),
                new TopicsCallback(project), null);
    }

    private void GetComments(Project mProject, Topic topic) {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET,
                APICall.GETComments(mProject, topic),
                new CommentsCallback(mProject, topic), null);
    }
    private void GetViewPoint(Project project, Comment comment) {
        NetworkConnManager.networkRequest(mContext, Request.Method.GET,
                APICall.GETViewpoint(project, comment.getMTopicGuid(), comment),
                new ViewPointCallback(project, comment), null);
    }

    // Private classes implements Volley Callbacks for specific API-calls

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
                topics = EntityListConstructor.Topics(jsonArray,mContext.getActiveProject().getProjectId());
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
     * add Snapshot, ViewPoint and Comment to topics
     */
    private class ViewPointCallback implements Callback<String> {
        Comment mComment;
        Project mProject;

        private ViewPointCallback(Project project, Comment comment){
            mComment = comment;
            mProject = project;
        }

        @Override
        public void onError(String response) {
            Log.d("SyncAdapter", "Error on ViewPoint callback. " + response);
        }

        @Override
        public void onSuccess(String response) {
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
                            APICall.GETSnapshot(mProject, mComment.getMTopicGuid(), vp),
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
}