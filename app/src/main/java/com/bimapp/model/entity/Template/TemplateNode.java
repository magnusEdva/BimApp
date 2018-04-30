package com.bimapp.model.entity.Template;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Individual nodes in the Templates List.
 */

public abstract class TemplateNode {

    public final static String VISIBLE = "visible";
    public final static String MANDATORY = "mandatory";

    /**
     * Title and also the description of what the particular
     * Node represents.
     */
    private String mNodeTitle;

    /**
     * whether the Node is visible or hidden in the view.
     */
    private boolean mVisible;
    /**
     * whether this field is mandatory
     */
    private boolean mMandatory;



    public TemplateNode(JSONObject node, String title){
        mNodeTitle = title;
        construct(node);
    }

    private void construct(JSONObject obj){
        try{
            mVisible = obj.getBoolean(VISIBLE);
            mMandatory = obj.getBoolean(MANDATORY);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public String getTitle(){
        return mNodeTitle;
    }

    public boolean isVisible(){
        return mVisible;
    }


    public abstract Object getContent();

    public abstract void setContent(String s);

}
