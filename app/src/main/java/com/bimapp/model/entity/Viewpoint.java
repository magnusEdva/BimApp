package com.bimapp.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.bimapp.model.Base64;

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

    public Viewpoint(){}

    public Viewpoint(JSONObject jsonObject, String guid) {
        mCommentGUID = guid;
        construct(jsonObject);
    }

    public Viewpoint(String type, Bitmap data, String guid) {
        mSnapshot = new Snapshot(type, data);
        hasSnapshot = true;
        mCommentGUID = guid;
    }

    public Viewpoint(String guid, String commentGUID, String type, String name){
        mGuid = guid;
        mCommentGUID = commentGUID;
        mSnapshot = new Snapshot(type, name);
    }

    public Viewpoint(ContentValues values){
        mGuid = values.getAsString(GUID);
        mCommentGUID = values.getAsString(COMMENT_GUID);
        mSnapshot = new Snapshot(values.getAsString("type"), values.getAsString("picture_name"));
    }


    private void construct(JSONObject jsonObject) {
        try {
            mGuid = jsonObject.getString(GUID);
            if (jsonObject.has(SNAPSHOT))
                hasSnapshot = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getMGuid() {
        return mGuid;
    }

    public Bitmap getSnapshot() {
        if(mSnapshot.image == null)
            mSnapshot.fetchPicture();
        return mSnapshot.image;
    }

    public Snapshot getMSnapshot(){return mSnapshot; }

    public void setGuid(String guid){
        mGuid = guid;
    }

    public void setSnapshot(Snapshot snapshot){
        mSnapshot = snapshot;
        mSnapshot.name = mGuid;
    }

    public void setCommentGUID(String commentGuid){
        mCommentGUID = commentGuid;
    }
    public String getMCommentGUID(){
        return mCommentGUID;
    }

    public boolean hasSnapshot() {

        return hasSnapshot;
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
        contentValues.put(GUID, getMGuid());
        contentValues.put(COMMENT_GUID, mCommentGUID);
        contentValues.put("type", mSnapshot.type);
        contentValues.put("picture_name", mSnapshot.name);
        return contentValues;
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

        public Snapshot(String type, String name){
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
         * Upon first initiating a Person stores their picture to disk
         * @param picture Bitmap to be stored
         * @param name candidate for this.picture, changed if already exists
         * @return
         */
        private String storePicture(Bitmap picture , String name){
            FileOutputStream out = null;
            try {
                Log.d("dir", dir + "/" + name);
                out = new FileOutputStream(dir + "/" + name);
                picture.compress(Bitmap.CompressFormat.PNG,100, out);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try{
                    if(out != null)
                        out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return name ;
        }

        public boolean deletePicture() {
            File picture = new File(dir + "/" + name);
            boolean deleted = false;
            if (!picture.exists()){
                Log.d("Picture", "No picture upon deleting " + name);
            }
            else
                deleted = picture.delete();
            return deleted;
        }

        /**
         * fetches Bitmap picture from file with this.picture as path.
         * @return Bitmap picture
         */
        private Bitmap fetchPicture(){
            FileInputStream fileIn = null;
            Bitmap picture = null;
            try{
                fileIn = new FileInputStream(dir + "/" + name.trim());
                picture = BitmapFactory.decodeStream(fileIn);
            } catch(Exception e){
                e.printStackTrace();
            } finally {

                try {
                    if(fileIn!= null)
                     fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return picture;
        }

        /**
         *
         * @param picture Bitmap to be stored
         * @param name candidate for this.picture
         * @return value for this.picture
         */
        private String pathNameValidity(Bitmap picture, String name, int number){
            File file = new File(dir + "/" + name + number);
            if(file.exists()){
                //change name slightly to make a new uniquely named file
                return pathNameValidity(picture, name, number++);
            }
            else
                return storePicture(picture, name + number);
        }


        public String convert() {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            try {
                return new String (Base64.getEncoder().encode(outputStream.toByteArray()), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }


    }
}
