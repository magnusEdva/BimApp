package com.bimapp.model.data_access.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bimapp.model.entity.Comment;

import java.util.List;

@Dao
public interface CommentDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Comment> comments);

    @Query("SELECT * FROM comment WHERE topic_guid = :topicGUID")
    List<Comment> getTopicsComments(String topicGUID);


}
