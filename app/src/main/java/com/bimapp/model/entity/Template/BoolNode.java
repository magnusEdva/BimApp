package com.bimapp.model.entity.Template;

import android.content.Context;
import android.view.View;

/**
 * Node which contains a bool of whether the
 * Node is a mandatory field in template or not.
 */

public class BoolNode extends TemplateNode{

    /**
     * is or is not mandatory when a new topic related to the template
     * is being made.
     */
    private boolean mMandatory;

    public BoolNode(String title, boolean visible, boolean mandatory) {
        super(title, visible);
        mMandatory = mandatory;
    }

    @Override
    public Object getContent() {
        return mMandatory;
    }

}
