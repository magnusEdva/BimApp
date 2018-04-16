package com.bimapp.model.entity.Template;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.bimapp.model.entity.Entity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This represents a template for a particular Topic type.
 * To be created within bimsyncs desktop client.
 */

public class Template implements Entity {
    private final static String PROPERTIES = "properties";
    public final static String TEMPLATE_NAME = "templateName";
    public final static String TEMPLATE_DESCRIPTION = "templateDescription";
    public final static String COLOR = "templateColor";
    //public final static String ICON = "icon";

    public final static String TITLE = "title";
    public final static String DESCRIPTION = "description";
    public final static String TOPIC_STATUS = "topic_status";
    public final static String TOPIC_TYPE = "topic_type";
    public final static String LABELS = "labels";
    public final static String ASSIGNED_TO = "assigned_to";
    public final static String DUE_DATE = "due_date";
    public final static String IMAGE = "image";
    public final static String COMMENT = "comment";


    private String mName;

    private String mDescription;

    private Integer mColor;

    private Integer mIcon;

    private List<TemplateNode> mNodes;

    public Template(JSONObject jsonTemplate) {
        mNodes = new ArrayList<>();
        try {
            construct(jsonTemplate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setProperties(JSONObject jsonTemplate) {
        if (jsonTemplate != null) {
            try {
                setProperties(jsonTemplate.getJSONObject(PROPERTIES));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void construct(JSONObject properties) {
        try {
            Iterator<String> iterator = properties.keys();
            String next;
            while (iterator.hasNext()) {
                next = iterator.next();
                switch (next) {
                    case TEMPLATE_NAME:
                        mName = properties.getString(next);
                        break;
                    case TEMPLATE_DESCRIPTION:
                        mDescription = properties.getString(next);
                        break;
                    case COLOR:
                        mColor = Color.parseColor((properties.getString(next)));
                        break;
                    case IMAGE:
                        mNodes.add(new ImageNode(properties.getJSONObject(next), next));
                        break;
                    case LABELS:
                        mNodes.add(new LabelsNode(properties.getJSONObject(next), next));
                        break;
                    default:
                        mNodes.add(new defaultNode(properties.getJSONObject(next), next));
                        break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return mName;
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

    public List<TemplateNode> getNodes() {
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
