package com.bimapp.model.entity.Template;

/**
 * Node to represent the 'description' field in a topic
 */
public class TopicDescriptionNode extends TemplateNode {

    /**
     * Description of the topic and if it should be visible
     */
    private String mDescription;

    /**
     * Constructor.
     * @param description the description you wish the topic to have
     * @param visible whether the description should be visible or not
     */
    public TopicDescriptionNode(String description, boolean visible){
        super(description,visible);
        mDescription = description;
    }

    @Override
    public Object getContent() {
        return mDescription;
    }
}
