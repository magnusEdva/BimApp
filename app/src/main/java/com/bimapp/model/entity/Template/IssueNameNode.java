package com.bimapp.model.entity.Template;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Node to represent the IssueName field of an Issue
 */
public class IssueNameNode extends TemplateNode {


    public IssueNameNode(String title, boolean visible) {
        super(title, visible);

    }

    @Override
    public Object getContent() {
        return null;
    }

    @Override
    public View makeView(Context context) {
        LinearLayout linearLayout = new LinearLayout(context);
        return null;
    }
}
