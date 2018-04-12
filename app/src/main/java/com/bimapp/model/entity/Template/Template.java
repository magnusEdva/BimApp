package com.bimapp.model.entity.Template;

import android.support.annotation.NonNull;

import com.bimapp.model.entity.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This represents a template for a particular Topic type.
 * To be created within bimsyncs desktop client.
 */

public class Template implements Entity {
    public final static String TITLE = "title";
    public final static String DESCRIPTION = "description";
    public final static String COLOR = "color";
    public final static String ICON = "icon";

    private String mTitle;

    private String mDescription;

    private Integer mColor;

    private Integer mIcon;

    private List<TemplateNode> mNodes;

    public Template(JSONObject jsonTemplate){
        mNodes = new ArrayList<>();
        construct(jsonTemplate);
    }
    private void construct(JSONObject jsonTemplate){
        try{
            mTitle = jsonTemplate.getString(TITLE);
            mDescription = jsonTemplate.getString(DESCRIPTION);
            mColor = jsonTemplate.getInt(COLOR);
            mIcon = jsonTemplate.getInt(ICON);

            mNodes.add(new TopicTitleNode("", true));
            mNodes.add(new TopicAssignedToNode("eriksen.hakon@gmail.com", true));
            mNodes.add(new TopicStatusNode("Open", false));
            mNodes.add(new TopicDescriptionNode("This is the filled in description", true));
            mNodes.add(new TopicTypeNode("", false));


        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public Integer getColor() {
        return mColor;
    }

    public Integer getIcon() {
        return mIcon;
    }

    public List<TemplateNode> getNodes(){
        return mNodes;
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
