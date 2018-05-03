package com.bimapp.model.data_access.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import com.bimapp.model.entity.Topic;

import java.util.List;

@Dao
public interface TopicDAO {

    @Query("SELECT * FROM topic WHERE project_id = :projectId AND topic_status != :status")
    Cursor getTopics(String projectId, String status);

    @Query("SELECT * FROM topic WHERE project_id = :projectId AND topic_status != :status AND title LIKE :SearchString")
    Cursor getTopics(String projectId, String SearchString, String status);

    @Query("SELECT * FROM topic WHERE status_column = :updatedOrNew  ORDER BY date_column ASC")
    Cursor getLocalTopics( String updatedOrNew);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Topic> topic);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert( Topic topic);

    @Query("DELETE FROM topic WHERE guid = :topicGUID")
    void delete(String topicGUID);
}
