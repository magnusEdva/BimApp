package com.bimapp.model.data_access.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bimapp.model.entity.Topic;

import java.util.List;

@Dao
public interface TopicDAO {

    @Query("SELECT * FROM topic WHERE project_id = :projectId")
    List<Topic> getTopics(String projectId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Topic> topic);
}
