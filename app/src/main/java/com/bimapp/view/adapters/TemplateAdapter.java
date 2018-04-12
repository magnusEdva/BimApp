package com.bimapp.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bimapp.R;
import com.bimapp.model.entity.Template.BoolNode;
import com.bimapp.model.entity.Template.IssueNameNode;
import com.bimapp.model.entity.Template.StringNode;
import com.bimapp.model.entity.Template.Template;
import com.bimapp.model.entity.Template.TemplateNode;
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
        ISSUE_NAME(1),
        DESCRIPTION(2),
        BOOL_NODE(3);

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

        public ViewHolder(View view) {
            super(view);
            mView = itemView;
            mLayout = itemView.findViewById(R.id.newtopic_list);
            this.mItem_description = itemView.findViewById(R.id.issue_description);
            this.mItem_input = itemView.findViewById(R.id.issue_description_input);
        }

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            switch (viewType) {
                case 1: // Issue NAME
                    this.mItem_description = (TextView) itemView.findViewById(R.id.issue_name);
                    this.mItem_input = itemView.findViewById(R.id.issue_name_input);
                    break;
                case 2: // Issue Description
                    this.mItem_description = itemView.findViewById(R.id.issue_description);
                    this.mItem_input = itemView.findViewById(R.id.issue_description_input);
                    break;
                case 3: // Issue Status
                    this.mItem_description = itemView.findViewById(R.id.issue_status);
                    this.mItem_input = itemView.findViewById(R.id.issue_status_input);
                    break;
                default: // Defaults to Issue status
                    this.mItem_description = itemView.findViewById(R.id.issue_status);
                    this.mItem_input = itemView.findViewById(R.id.issue_status_input);
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
     * Checks the templateList for what view should be assosciated with that position
     *
     * @param position the position to
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        Object c = mList.get(position);
        if (c instanceof IssueNameNode) {
            return NODE_TYPE.ISSUE_NAME.getInt();
        } else if (c instanceof StringNode)
            return NODE_TYPE.DESCRIPTION.getInt();
        else if (c instanceof BoolNode)
            return NODE_TYPE.BOOL_NODE.getInt();
        else
            return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        ViewHolder viewHolder;

        switch (viewType) {
            case 1: // Issue NAME
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.issue_name, parent, false);
                viewHolder = new ViewHolder(view, viewType);
                break;
            case 2: // Issue DESCRIPTION
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.issue_description, parent, false);
                viewHolder = new ViewHolder(view, viewType);
                break;
            case 3: // Issue status
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.issue_status,parent,false);
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.issue_status, parent, false);
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
                holder.mItem_input.setText(R.string.issue_name_input);
                break;
            case 2: // IssueDescription
                holder.mItem_description.setText(R.string.issue_description);
                holder.mItem_input.setText(R.string.description_inpu);
                break;
            case 3: // IssueStatus
                holder.mItem_description.setText(R.string.issue_status);
                holder.mItem_input.setText(R.string.issue_status);
            default: // Issue Description
                holder.mItem_description.setText(mList.get(position).getTitle());
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
