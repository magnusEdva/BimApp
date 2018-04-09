package com.bimapp.model.entity.Template;

import android.support.annotation.NonNull;

import com.bimapp.model.entity.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * This represents a template for a particular Topic type.
 * To be created within bimsyncs desktop client.
 */

public class Template implements Entity {
    public final static String TITLE = "title";
    public final static String DESCRIPTION = "description";

    private String mTitle;

    private String mDescription;

    private Integer mColor;

    private Integer mIcon;

    private List<TemplateNode> mNodes;

    public Template(JSONObject jsonTemplate){
        construct(jsonTemplate);
    }
    private void construct(JSONObject jsonTemplate){
        try{
            mTitle = jsonTemplate.getString(TITLE);
            mDescription = jsonTemplate.getString(DESCRIPTION);
        }catch (JSONException e){

        }
    }
    @Override
    public Map<String, String> getStringParams(@NonNull Map<String, String> map) {
        return null;
    }

    @Override
    public JSONObject getJsonParams() {
        return null;
    }


}
