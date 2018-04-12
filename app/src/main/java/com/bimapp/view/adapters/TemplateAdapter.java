package com.bimapp.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bimapp.R;
import com.bimapp.model.entity.Template.TopicAssignedToNode;
import com.bimapp.model.entity.Template.TopicDescriptionNode;
import com.bimapp.model.entity.Template.TopicStatusNode;
import com.bimapp.model.entity.Template.TopicTitleNode;
import com.bimapp.model.entity.Template.Template;
import com.bimapp.model.entity.Template.TemplateNode;
import com.bimapp.model.entity.Template.TopicTypeNode;
import com.bimapp.view.interfaces.NewTopicViewInterface;

import java.util.ArrayList;
import java.util.List;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {

    private Template mTemplate;
    private List<TemplateNode> mList;
    private final NewTopicViewInterface mListener;

    /**
     * Enum to separate node types
     */
    private static enum NODE_TYPE {
        TITLE(1),
        DESCRIPTION(2),
        TOPIC_STATUS(3),
        TOPIC_TYPE(4),
        ASSIGNED_TO(5),
        ;

        private int i;

        private NODE_TYPE(int i) {
            this.i = i;
        }

        public int getInt() {
            return i;
        }
    }

    /**
     * Provide a reference to the views for each item
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout mLayout;
        public View mView;


        public final TextView mItem_description;
        public final EditText mItem_input;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            switch (viewType) {
                case 1: // TITLE
                    this.mItem_description = itemView.findViewById(R.id.topic_title);
                    this.mItem_input = itemView.findViewById(R.id.topic_title_input);
                    break;
                case 2: // DESCRIPTION
                    this.mItem_description = itemView.findViewById(R.id.topic_description);
                    this.mItem_input = itemView.findViewById(R.id.topic_description_input);
                    break;
                case 3: // TOPIC_STATUS
                    this.mItem_description = itemView.findViewById(R.id.topic_status);
                    this.mItem_input = itemView.findViewById(R.id.topic_status_input);
                    break;
                case 4: // TOPIC_TYPE
                    this.mItem_description = itemView.findViewById(R.id.topic_type);
                    this.mItem_input = itemView.findViewById(R.id.topic_type_input);
                    break;
                case 5: // ASSIGNED_TO
                    this.mItem_description = itemView.findViewById(R.id.topic_assigned_to);
                    this.mItem_input = itemView.findViewById(R.id.topic_assigned_to_input);
                    break;
                default: // Defaults to no view
                    this.mItem_description = itemView.findViewById(R.id.topic_status);
                    this.mItem_input = itemView.findViewById(R.id.topic_status_input);
                    break;
            }
            mLayout = itemView.findViewById(R.id.newtopic_list);
            mView = itemView;
        }
    }

    public TemplateAdapter(Template template, NewTopicViewInterface listener) {
        mList = new ArrayList<>();
        mList.addAll(template.getNodes());
        mListener = listener;
    }

    /**
     * Checks the templateList for what view should be associated with that position
     *
     * @param position the position to
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        Object c = mList.get(position);
        if (c instanceof TopicTitleNode)
            return NODE_TYPE.TITLE.getInt();
        else if (c instanceof TopicDescriptionNode)
            return NODE_TYPE.DESCRIPTION.getInt();
        else if (c instanceof TopicStatusNode)
            return NODE_TYPE.TOPIC_STATUS.getInt();
        else if (c instanceof TopicTypeNode)
            return NODE_TYPE.TOPIC_TYPE.getInt();
        else if (c instanceof TopicAssignedToNode)
            return NODE_TYPE.ASSIGNED_TO.getInt();
        else
            return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        ViewHolder viewHolder;

        switch (viewType) {
            case 1: // TITLE
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.topic_title, parent, false);
                viewHolder = new ViewHolder(view, viewType);
                break;
            case 2: // DESCRIPTION
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.topic_description, parent, false);
                viewHolder = new ViewHolder(view, viewType);
                break;
            case 3: // TOPIC_STATUS
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.topic_status,parent,false);
                viewHolder = new ViewHolder(view, viewType);
                break;
            case 4: // TOPIC_TYPE
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.topic_type,parent,false);
                viewHolder = new ViewHolder(view, viewType);
                break;
            case 5: // ASSIGNED_TO
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_assignedto, parent, false);
                viewHolder = new ViewHolder(view, viewType);
                break;
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.topic_status, parent, false);
                viewHolder = new ViewHolder(view, viewType);
                break;
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (this.getItemViewType(position)) {
            case 1: // IssueName
                holder.mItem_description.setText(R.string.issue_name);
                holder.mItem_input.setText(mList.get(position).getContent().toString());
                break;
            case 2: // IssueDescription
                holder.mItem_description.setText(R.string.issue_description);
                holder.mItem_input.setText(mList.get(position).getContent().toString());
                break;
            case 3: // IssueStatus
                holder.mItem_description.setText(R.string.issue_status);
                holder.mItem_input.setText(mList.get(position).getContent().toString());
                break;
            case 4: // TOPIC_TYPE
                holder.mItem_description.setText(R.string.topice_type);
                holder.mItem_input.setText(mList.get(position).getContent().toString());
                break;
            case 5: // Assigned to
                holder.mItem_description.setText(R.string.assigned_to);
                holder.mItem_input.setText(mList.get(position).getContent().toString());
                break;
            default: // Issue Description
                holder.mItem_description.setText("Default");
                holder.mItem_input.setText("Default text");
                break;

        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setTemplate(Template template) {
        if (!mList.isEmpty())
            mList.clear();
        mList.addAll(template.getNodes());
    }

}
