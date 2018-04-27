package com.bimapp.model.data_access;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bimapp.model.entity.Comment;

public class DataProvider extends ContentProvider {
    public static final String CommentTable = "comment_table";
    public static final String ProjectTable = "Project_table";
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
        Log.e("DATAPROVIED", selection);
        Cursor cursor = null;
        switch (uri.getPath()){
            case("/" + CommentTable):
                cursor = database.commentDao().getTopicsComments(selection);
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
        Log.e("DATAPROVIED", values.toString());
        Cursor cursor = null;
        switch (uri.getPath()){
            case("/" + CommentTable):
                database.commentDao().insert(new Comment(values));
                break;
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
