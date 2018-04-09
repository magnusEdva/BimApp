package com.bimapp.model.entity.Template;

/**
 * Node which contains String default content.
 */

public class StringNode extends TemplateNode {

    private String mDefaultContent;

    public StringNode(String title, boolean visible, String defaultContent) {
        super(title, visible);
        mDefaultContent = defaultContent;
    }

    @Override
    public Object getContent() {
        return mDefaultContent;
    }
}
