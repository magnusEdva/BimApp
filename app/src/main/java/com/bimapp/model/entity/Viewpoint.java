package com.bimapp.model.entity;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Viewpoint implements Entity {
    public final static String GUID = "guid";
    private Snapshot mSnapshot;
    private String mGuid;

    public Viewpoint(String type, String data) {
        mSnapshot = new Snapshot(type, data);
    }

    public Viewpoint(JSONObject jsonObject){
        contruct(jsonObject);
    }

    private void contruct(JSONObject jsonObject) {
        try {
            mGuid = jsonObject.getString(GUID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getGuid(){
        return mGuid;
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
            snapshot.put("snapshot_data", mSnapshot.data);
            viewpoints.put("snapshot", snapshot);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return viewpoints;
    }

    private class Snapshot {
        String type;
        String data;

        Snapshot(String type, String data) {
            this.data = data;
            this.type = type;
        }

    }
}
