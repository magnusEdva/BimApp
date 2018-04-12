package com.bimapp.model.entity.Template;

/**
 * Class to represent the 'topic_status' field in a topic
 */
public class TopicStatusNode extends TemplateNode {

    /**
     * status of the topic (Valid values from extensions.topic_status)
     */
    private String mTopicStatus;

    /**
     * Constructor
     * @param status the status this topic should have (Should probably not be a string, but an enum or something
     * @param visible whether this field should be visible or not
     */
    public TopicStatusNode(String status, boolean visible){
        super(status,visible);
        mTopicStatus = status;
    }

    @Override
    public Object getContent() {
        return mTopicStatus;
    }
}
