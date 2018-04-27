package com.bimapp.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;

import com.bimapp.model.Base64;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class Viewpoint implements entity {
    public final static String GUID = "guid";
    public final static String SNAPSHOT = "snapshot";
    public final static String SNAPSHOT_TYPE = "snapshot_type";
    public final static String SNAPSHOT_DATA = "snapshot_data";


    public final static String SNAPSHOT_TYPE_JPG = "jpg";
    public final static String SNAPSHOT_TYPE_PNG = "png";

    @Embedded
    private Snapshot mSnapshot;
    @ColumnInfo(name = GUID)
    private String mGuid;
    @Ignore
    private boolean hasSnapshot;

    public Viewpoint(String type, Bitmap data) {
        mSnapshot = new Snapshot(type, data);
        hasSnapshot = true;
    }

    public Viewpoint(){}

    public Viewpoint(JSONObject jsonObject) {
        construct(jsonObject);
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
        return mSnapshot.image;
    }

    public Snapshot getMSnapshot(){return mSnapshot; }

    public void setGuid(String guid){
        mGuid = guid;
    }

    public void setSnapshot(Snapshot snapshot){
        mSnapshot = snapshot;
    }

    public boolean hasSnapshot() {

        return hasSnapshot;
    }

    public void constructSnapshot(Bitmap snapshot) {
        mSnapshot = new Snapshot(snapshot);
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
    @Entity(tableName = "snapshot")
    public static class Snapshot {
        @Ignore
        public Bitmap image;
        @ColumnInfo(name = "type")
        public String type;

        Snapshot(String type, Bitmap data) {
            this.type = type;
            image = data;
        }

        public Snapshot(){}

        Snapshot(Bitmap data) {
            image = data;
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
