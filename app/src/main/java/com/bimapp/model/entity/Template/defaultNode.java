package com.bimapp.model.entity.Template;

import org.json.JSONObject;

public class defaultNode extends TemplateNode {
    public final static String DEFAULT_VALUE = "defaultValue";


    private String defaultValue;

    public defaultNode(JSONObject node, String title) {
        super(node, title);
        assignDefaultValue(node);
    }

    public void assignDefaultValue(JSONObject node){
        try{
            if(node.has(DEFAULT_VALUE) && !node.getString(DEFAULT_VALUE).equals("null"))
                defaultValue = node.getString(DEFAULT_VALUE);
            else
                defaultValue = "";
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public Object getContent() {
        return defaultValue;
    }

    @Override
    public void setContent(String s) {
        defaultValue = s;
    }

}
