package com.bimapp.model.entity.Template;

/**
 * Class to represent the 'assigned_to' field in a topic
 */
public class TopicAssignedToNode extends TemplateNode {

    /**
     * Email of the assigned user. Valid values from extensions.user_id_type in the API
     */
    private String mAssignedTo;

    /**
     * Constructor
     * @param assignedTo the email of the person you want this topic to be assigned to
     * @param visible whether this field should be visible or not
     */
    public TopicAssignedToNode(String assignedTo, boolean visible){
        super(assignedTo, visible);
        mAssignedTo = assignedTo;
    }

    @Override
    public Object getContent() {
        return mAssignedTo;
    }


}
