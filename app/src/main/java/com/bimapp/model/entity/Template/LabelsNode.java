package com.bimapp.model.entity.Template;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LabelsNode extends TemplateNode {

    private List<String> labels;

    public LabelsNode(JSONObject node, String title) {
        super(node, title);
    }

    private void constructLabels(JSONObject node){
        try{
            if(node.has(Template.LABELS)){
                JSONArray arr = node.getJSONArray(Template.LABELS);
                labels = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++){
                    labels.add(arr.getString(i));
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public Object getContent() {
        return labels;
    }
}
