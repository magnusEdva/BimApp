package com.bimapp.model.entity.Template;

import android.content.Context;
import android.view.View;

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

    /**
     * The view associated with this node
     */
    private View mView;



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

    public View getView(){
        return mView;
    }

    public abstract Object getContent();

    /**
     * Each type of node must construct a view associated with this node.
     * @return the View that matches the node.
     */
    public abstract View makeView(Context context);
}
