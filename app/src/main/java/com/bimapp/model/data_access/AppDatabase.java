package com.bimapp.model.data_access;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import com.bimapp.model.data_access.dao.CommentDAO;
import com.bimapp.model.data_access.dao.ProjectDAO;
import com.bimapp.model.data_access.dao.TopicDAO;
import com.bimapp.model.data_access.dao.UserDAO;
import com.bimapp.model.data_access.dao.ViewpointDAO;
import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.DateMapper;
import com.bimapp.model.entity.Project;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.entity.User;
import com.bimapp.model.entity.Viewpoint;

@Database(entities = {Comment.class, Topic.class, User.class, Project.class, Viewpoint.class}, version = 8)
@TypeConverters({Topic.class, DateMapper.class})
public abstract class AppDatabase extends RoomDatabase {
    /**
     * a date column contains the date of
     * when an item was inserted into the database.
     * Used for synchronising with the API in posting order.
     */
    public static final String DATE_COLUMN = "date_column";
    /**
     * A status column shows wether a row is posted to the server or not.
     * Has value from statusTypes.
     */
    public static final String STATUS_COLUMN = "status_column";

    /**
     * New represents an item created on the local device, not yet posted
     * to the remote server.
     *
     * updated represents an item that has been changed by the local device, but without having
     * the change registered remotely.
     *
     * live represents unchanged data from the server. But it is not necessarily up to date.
     */
    public enum statusTypes{
        New("NEW"), updated("UPDATED"), live("LIVE");

        public final String status;

        statusTypes(String status){
            this.status = status;
        }
    }

    private static AppDatabase sInstance;

    public static final String DATABASE_NAME = "bimapp_db";

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public abstract ProjectDAO projectDAO();

    public abstract CommentDAO commentDao();

    public abstract TopicDAO topicDao();

    public abstract UserDAO userDao();

    public abstract ViewpointDAO viewpointDAO();

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(context.getApplicationContext());
                    sInstance.updateDatabaseCreated(context.getApplicationContext());

                }
            }
        }
        return sInstance;
    }
    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                            AppDatabase database = AppDatabase.getInstance(appContext);
                            database.setDatabaseCreated();

                    }
                }).fallbackToDestructiveMigration().build();
    }

    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

    @TypeConverter
    public static statusTypes convertStringToStatus(String status){
        statusTypes convertedValue = null;
        for (int i = 0; i < statusTypes.values().length && convertedValue == null; i++){
            if(statusTypes.values()[i].status.equals(status))
                convertedValue = statusTypes.values()[i];
        }
        return convertedValue;
    }

    @TypeConverter
    public static String convertStatusToString(statusTypes status){
        return status.status;
    }
}
