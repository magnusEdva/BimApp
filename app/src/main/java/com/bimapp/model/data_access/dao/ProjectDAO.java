package com.bimapp.model.data_access.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import com.bimapp.model.entity.Project;

import java.util.List;

@Dao
public interface ProjectDAO {
    @Query("SELECT * FROM project where bimsync_project_id = :bimsyncId")
    LiveData<List<Project>> loadProjects(int bimsyncId);

    @Query("SELECT * FROM project where bimsync_project_id = :bimsyncId")
    Cursor loadProjectsSync(int bimsyncId);

    @Query("SELECT * FROM project where project_id = :projectId")
    Cursor loadBCFproject(int projectId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Project> projects);
}

