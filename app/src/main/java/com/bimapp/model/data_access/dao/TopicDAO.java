package com.bimapp.model.data_access.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import com.bimapp.model.entity.Topic;

import java.util.List;

@Dao
public interface TopicDAO {

    @Query("SELECT * FROM topic WHERE project_id = :projectId")
    Cursor getTopics(String projectId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Topic> topic);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert( Topic topic);
}
