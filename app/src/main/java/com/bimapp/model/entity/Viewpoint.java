package com.bimapp.model.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public class Viewpoint implements Entity {
    public final static String GUID = "guid";
    public final static String SNAPSHOT = "snapshot";
    public final static String SNAPSHOT_TYPE = "snapshot_type";
    public final static String SNAPSHOT_data = "snapshot_type";


    private Snapshot mSnapshot;
    private String mGuid;
    private boolean hasSnapshot = false;

    public Viewpoint(String type, Bitmap data) {
        mSnapshot = new Snapshot(data);
    }

    public Viewpoint(JSONObject jsonObject) {
        contruct(jsonObject);
    }

    private void contruct(JSONObject jsonObject) {
        try {
            mGuid = jsonObject.getString(GUID);
            if (jsonObject.has(SNAPSHOT))
                hasSnapshot = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getGuid() {
        return mGuid;
    }

    public Bitmap getSnapshot() {
        return mSnapshot.image;
    }

    public boolean hasSnapshot() {

        return hasSnapshot;
    }

    public void constructSnapshot(Bitmap snapshot) {
            mSnapshot = new Snapshot(snapshot);
    }

    @Override
    public Map<String, String> getStringParams(@NonNull Map<String, String> map) {
        return null;
    }

    @Override
    public JSONObject getJsonParams() {
        JSONObject viewpoints = new JSONObject();
        JSONObject snapshot = new JSONObject();
        try {
            snapshot.put("snapshot_type", mSnapshot.type);
            snapshot.put("snapshot_data", mSnapshot.getImageBytesAsString());
            viewpoints.put("snapshot", snapshot);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return viewpoints;
    }

    private class Snapshot {
        Bitmap image;
        String type = "png";

        Snapshot( Bitmap data) {
            image = data;
        }



        private String getImageBytesAsString() {
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 0 /* Ignored for PNGs */, blob);
            byte[] bitmapdata = blob.toByteArray();
            return new String(bitmapdata);
        }


    }
}
