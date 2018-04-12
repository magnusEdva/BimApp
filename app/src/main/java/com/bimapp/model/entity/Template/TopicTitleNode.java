package com.bimapp.model.entity.Template;

/**
 * Node to represent the 'title' field of an Issue
 */
public class TopicTitleNode extends TemplateNode {

    /**
     * Title of the topic, always MANDATORY
     */
    private String mIssueName;


    /**
     * Constructor
     * @param title the title of the topic
     * @param visible whether this field should be visible or not (should always be visible since
     *                it is mandatory?)
     */
    public TopicTitleNode(String title, boolean visible) {
        super(title, visible);
        mIssueName = title;

    }

    @Override
    public Object getContent() {
        return mIssueName;
    }

}
