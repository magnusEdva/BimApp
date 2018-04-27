package com.bimapp.model.data_access.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.content.ContentValues;
import android.database.Cursor;

import com.bimapp.model.entity.Comment;

import java.util.List;

@Dao
public interface CommentDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Comment ... comments);

    @Query("SELECT * FROM comment WHERE topic_guid = :topicGUID")
    Cursor getTopicsComments(String topicGUID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Comment comment);
}
