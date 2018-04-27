package com.bimapp.model.data_access.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.bimapp.model.entity.Project;

import java.util.List;

@Dao
public interface ProjectDAO {
    @Query("SELECT * FROM project where bimsync_project_id = :bimsyncId")
    LiveData<List<Project>> loadProjects(int bimsyncId);

    @Query("SELECT * FROM project where bimsync_project_id = :bimsyncId")
    List<Project> loadProjectsSync(int bimsyncId);

    @Query("SELECT * FROM project where project_id = :projectId")
    List<Project> loadBCFproject(int projectId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Project> projects);
}

