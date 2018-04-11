package com.bimapp.model.entity.Template;

/**
 * Node to represent the IssueName field of an Issue
 */
public class IssueNameNode extends TemplateNode {

    private String mIssueName;


    public IssueNameNode(String title, boolean visible) {
        super(title, visible);
        mIssueName = title;

    }

    @Override
    public Object getContent() {
        return mIssueName;
    }

}
