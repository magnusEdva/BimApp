package com.bimapp.model.entity.Template;

/**
 * Individual nodes in the Templates List.
 */

public abstract class TemplateNode {

    /**
     * Title and also the description of what the particular
     * Node represents.
     */
    private String mNodeTitle;

    /**
     * whether the Node is visible or hidden in the view.
     */
    private boolean mVisible;



    public TemplateNode(String title, boolean visible){
        mNodeTitle = title;
        mVisible = visible;
    }

    public String getTitle(){
        return mNodeTitle;
    }

    public boolean isVisible(){
        return mVisible;
    }

    public abstract Object getContent();
}
