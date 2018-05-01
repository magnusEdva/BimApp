package com.bimapp.model.data_access.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Viewpoint;
@Dao
public interface ViewpointDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Viewpoint... viewpoints);

    @Query("SELECT * FROM viewpoint WHERE commentGUID = :commentGUID")
    Cursor getViewpointForComment(String commentGUID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Viewpoint viewpoint);

    @Query("DELETE FROM viewpoint WHERE guid = :guid")
    void deleteViewpoint(String guid);
}
