package com.bimapp.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.bimapp.model.Base64;
import com.bimapp.model.data_access.AppDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "viewpoint")
public class Viewpoint implements entity {
    public final static String GUID = "guid";
    public final static String COMMENT_GUID = "commentGUID";
    public final static String SNAPSHOT = "snapshot";
    public final static String SNAPSHOT_TYPE = "snapshot_type";
    public final static String SNAPSHOT_DATA = "snapshot_data";


    public final static String SNAPSHOT_TYPE_JPG = "jpg";
    public final static String SNAPSHOT_TYPE_PNG = "png";

    @Embedded
    private Snapshot mSnapshot;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = GUID)
    private String mGuid;
    @Ignore
    private boolean hasSnapshot;
    @ColumnInfo(name = COMMENT_GUID)
    private String mCommentGUID;


    @ColumnInfo(name = AppDatabase.DATE_COLUMN)
    private Long dateAcquired;
    @TypeConverters(AppDatabase.class)
    @ColumnInfo(name = AppDatabase.STATUS_COLUMN)
    private AppDatabase.statusTypes localStatus;

    public Viewpoint() {
    }

    public Viewpoint(JSONObject jsonObject, String guid) {
        mCommentGUID = guid;
        construct(jsonObject);
        dateAcquired = System.currentTimeMillis();
        localStatus = AppDatabase.statusTypes.live;
    }

    public Viewpoint(String type, Bitmap data, String guid) {
        mSnapshot = new Snapshot(type, data);
        hasSnapshot = true;
        mCommentGUID = guid;
        dateAcquired = System.currentTimeMillis();
        localStatus = AppDatabase.statusTypes.New;
        mGuid = Math.random() + "";
    }

    public Viewpoint(String guid, String commentGUID, String type, String name,
                     Long dateAcquired, AppDatabase.statusTypes localStatus) {
        mGuid = guid;
        mCommentGUID = commentGUID;
        mSnapshot = new Snapshot(type, name);
        hasSnapshot = true;
        this.dateAcquired = dateAcquired;
        this.localStatus = localStatus;
    }

    public Viewpoint(ContentValues values) {
        mGuid = values.getAsString(GUID);
        mCommentGUID = values.getAsString(COMMENT_GUID);
        dateAcquired = values.getAsLong(AppDatabase.DATE_COLUMN);
        localStatus = AppDatabase.convertStringToStatus(values.getAsString(AppDatabase.STATUS_COLUMN));
        mSnapshot = new Snapshot(values.getAsString("type"), values.getAsString("picture_name"));
    }


    private void construct(JSONObject jsonObject) {
        try {
            mGuid = jsonObject.getString(GUID);
            if (jsonObject.has(SNAPSHOT)) {
                hasSnapshot = true;
                mSnapshot = new Snapshot(SNAPSHOT_TYPE_PNG, mGuid);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getMGuid() {
        return mGuid;
    }

    public Bitmap getSnapshot() {
        if(mSnapshot == null)
            mSnapshot = new Snapshot(SNAPSHOT_TYPE_PNG, mGuid);
        if ( mSnapshot.image == null)
            mSnapshot.fetchPicture();
        return mSnapshot.image;
    }

    public Snapshot getMSnapshot() {
        return mSnapshot;
    }

    public void setGuid(String guid) {
        mGuid = guid;
    }

    public void setSnapshot(Snapshot snapshot) {
        mSnapshot = snapshot;
        mSnapshot.name = mGuid;
    }

    public void setCommentGUID(String commentGuid) {
        mCommentGUID = commentGuid;
    }

    public String getMCommentGUID() {
        return mCommentGUID;
    }

    public boolean hasSnapshot() {

        return hasSnapshot;
    }

    public void setDateAcquired(Long date) {
        dateAcquired = date;
    }

    public Long getDateAcquired() {
        return dateAcquired;
    }

    public void setLocalStatus(AppDatabase.statusTypes date) {
        localStatus = date;
    }

    public AppDatabase.statusTypes getLocalStatus() {
        return localStatus;
    }

    public void constructSnapshot(Bitmap snapshot) {
        mSnapshot = new Snapshot(snapshot, mGuid);
    }

    @Override
    public JSONObject getJsonParams() {
        JSONObject viewpoints = new JSONObject();
        JSONObject snapshot = new JSONObject();
        try {
            snapshot.put(SNAPSHOT_TYPE, SNAPSHOT_TYPE_PNG);
            snapshot.put(SNAPSHOT_DATA, mSnapshot.convert());
            viewpoints.put(SNAPSHOT, snapshot);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return viewpoints;
    }

    public ContentValues getContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GUID, mGuid);
        contentValues.put(COMMENT_GUID, mCommentGUID);
        contentValues.put("type", mSnapshot.type);
        contentValues.put("picture_name", mSnapshot.name);
        contentValues.put(AppDatabase.DATE_COLUMN, dateAcquired);
        contentValues.put(AppDatabase.STATUS_COLUMN, AppDatabase.convertStatusToString(localStatus));
        return contentValues;
    }

    public Boolean checkIfImageIsAlreadyStored() {
        File file = new File(Snapshot.dir + "/" + mGuid);
        return file.exists();

    }


    @Entity(tableName = "snapshot")
    public static class Snapshot {
        @Ignore
        public Bitmap image;
        @ColumnInfo(name = "type")
        public String type;
        @ColumnInfo(name = "picture_name")
        public String name;

        Snapshot(String type, Bitmap data) {
            this.type = type;
            image = data;
        }

        public Snapshot(String type, String name) {
            this.type = type;
            this.name = name;
        }

        Snapshot(Bitmap data, String guid) {
            type = "jpg";
            image = data;
            name = guid;
            storePicture(image, name);
        }

        public static String dir;

        /**
         * Upon first initiating a snapshot stores its image to disk
         *
         * @param picture Bitmap to be stored
         * @param name    viewpoint GUID for unique id
         * @return
         */
        private String storePicture(Bitmap picture, String name) {
            File file = new File(dir + "/" + name);
            if (file.exists()) {
                return name;
            } else {
                FileOutputStream out = null;
                try {
                    Log.d("dir", dir + "/" + name);
                    out = new FileOutputStream(dir + "/" + name);
                    picture.compress(Bitmap.CompressFormat.PNG, 100, out);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null)
                            out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return name;
            }
        }

        public boolean deletePicture() {
            File picture = new File(dir + "/" + name);
            boolean deleted = false;
            if (!picture.exists()) {
                Log.d("Picture", "No picture upon deleting " + name);
            } else
                deleted = picture.delete();
            return deleted;
        }

        /**
         * fetches Bitmap picture from file with name as path.
         */
        private void fetchPicture() {

            FileInputStream fileIn = null;
            Bitmap picture = null;
            try {
                fileIn = new FileInputStream(dir + "/" + name);
                picture = BitmapFactory.decodeStream(fileIn);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                try {
                    if (fileIn != null)
                        fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            image = picture;
        }

        public String convert() {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            try {
                return new String(Base64.getEncoder().encode(outputStream.toByteArray()), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }


    }
}
