package com.bimapp.model.data_access;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Project;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.entity.Viewpoint;

public class DataProvider extends ContentProvider {
    public static final String COMMENT_TABLE = "comment_table";
    public static final String PROJECT_TABLE = "Project_table";
    public static final String VIEWPOINT_TABLE = "viewpoint_table";
    public static final String TOPIC_TABLE = "topic_table";
    public static final String LOCAL_ROWS = "local_rows";
    public static final String SEARCH = "search";
    public static final String ASSIGNED_TO = "assigned_to";


    public static final String AUTHORITY = "com.bimapp.model.data_access.DataProvider";

    public AppDatabase database;

    @Override
    public boolean onCreate() {
        database = AppDatabase.getInstance(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        switch (uri.getPath()) {
            case ("/" + COMMENT_TABLE):
                if (selectionArgs != null && selectionArgs[0].equals(LOCAL_ROWS))
                    if (selectionArgs.length ==1)
                        cursor = database.commentDao().getNewComments(selection);
                else
                    cursor = database.commentDao().getTopicsComments(selection, selectionArgs[1]);
                else
                    cursor = database.commentDao().getTopicsComments(selection);
                break;
            case ("/" + VIEWPOINT_TABLE):
                cursor = database.viewpointDAO().getViewpointForComment(selection);
                break;
            case ("/" + PROJECT_TABLE):
                cursor = database.projectDAO().loadBCFproject();
                break;
            case ("/" + TOPIC_TABLE):
                if (selectionArgs != null && selectionArgs[0].equals(LOCAL_ROWS))
                    cursor = database.topicDao().getLocalTopics(selection);
                else if(selectionArgs != null && selectionArgs[0].equals(SEARCH))
                    cursor = database.topicDao().getTopics(selection, selectionArgs[1], "");
                else if(selectionArgs != null && selectionArgs[0].equals(ASSIGNED_TO))
                    cursor = database.topicDao().getMyTopics(selection, selectionArgs[1], "Closed");
                else
                    cursor = database.topicDao().getTopics(selection, "Closed");
                break;

        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (uri.getPath()) {
            case ("/" + COMMENT_TABLE):
                database.commentDao().insert(new Comment(values));
                break;
            case ("/" + VIEWPOINT_TABLE):
                database.viewpointDAO().insert(new Viewpoint(values));
                break;
            case ("/" + PROJECT_TABLE):
                database.projectDAO().insert(new Project(values));
                break;
            case ("/" + TOPIC_TABLE):
                database.topicDao().insert(new Topic(values));
                break;
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (uri.getPath()) {
            case ("/" + TOPIC_TABLE):
                database.topicDao().delete(selection);
                break;
            case ("/" + COMMENT_TABLE):
                database.commentDao().delete(selection);
                break;
            case ("/" + VIEWPOINT_TABLE):
                database.viewpointDAO().deleteViewpoint(selection);
                break;
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    public static Uri ParseUri(String path) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("content");
        builder.authority(DataProvider.AUTHORITY);
        builder.path(path);
        return builder.build();
    }
}
