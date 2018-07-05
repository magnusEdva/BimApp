package com.bimapp.model.data_access.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import com.bimapp.model.entity.Comment;

import java.util.List;

@Dao
public interface CommentDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Comment ... comments);

    @Query("SELECT * FROM comment WHERE topic_guid = :topicGUID")
    LiveData<List<Comment>> getTopicsCommentsLive(String topicGUID);

    @Query("SELECT * FROM comment WHERE topic_guid = :topicGUID")
    Cursor getTopicsComments(String topicGUID);

    @Query(("SELECT * FROM comment WHERE topic_guid = :topicGUID AND status_column = :status ORDER BY date_column ASC"))
    Cursor getTopicsComments(String topicGUID, String status);

    @Query(("SELECT * FROM comment WHERE status_column = :status ORDER BY date_column ASC"))
    Cursor getNewComments(String status);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Comment comment);

    @Query("DELETE FROM comment WHERE guid = :commentGUID")
    void delete(String commentGUID);
}
