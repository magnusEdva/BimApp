package com.bimapp.model.entity.Template;

/**
 * Class to represent the 'topic_type' field in a topic
 */
public class TopicTypeNode extends TemplateNode {

    /**
     * type of the topic (Valid values from extensions.topic_type)
     */
    private String mTopic_type;

    public TopicTypeNode (String type, boolean visible){
        super(type, visible);
        mTopic_type = type;
    }


    @Override
    public Object getContent() {
        return mTopic_type;
    }
}
