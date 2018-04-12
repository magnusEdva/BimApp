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

import java.util.List;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {

    private Template mTemplate;
    private List<TemplateNode>  mList;
    private final NewTopicViewInterface mListener;

    /**
     * Enum to separate node types
     */
    private static enum NODE_TYPE {
        ISSUE_NAME(1),
        DESCRIPTION(2),
        BOOL_NODE(3);

        private int i;

        private NODE_TYPE(int i){
            this.i = i;
        }

        public int getInt(){
            return i;
        }
    }

    /**
     * Provide a reference to the views for each item
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final LinearLayout mLayout;
        public View mView;


        public TextView mItem_description;
        public EditText mItem_input;


        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            switch (viewType){
                case 1: // Issue NAME
                    this.mItem_description = (TextView) itemView.findViewById(R.id.issue_name);
                    this.mItem_input = itemView.findViewById(R.id.issue_name_input);
                    break;
                case 2: // Issue Description
                    this.mItem_description = itemView.findViewById(R.id.issue_description);
                    this.mItem_input = itemView.findViewById(R.id.issue_description_input);
                    break;
                case 3:
                    this.mItem_description = itemView.findViewById(R.id.issue_description);
                    this.mItem_input = itemView.findViewById(R.id.issue_description_input);
                    break;
                    default:
                        this.mItem_description = itemView.findViewById(R.id.issue_description);
                        this.mItem_input = itemView.findViewById(R.id.issue_description_input);
                        break;
            }
            mLayout = itemView.findViewById(R.id.newtopic_list);
            mView = itemView;
        }
    }

    public TemplateAdapter (Template template, NewTopicViewInterface listener){
        mList = template.getNodes();
        mListener = listener;
    }

    /**
     * Checks the templateList for what view should be assosciated with that position
     * @param position the position to
     * @return
     */
    @Override
    public int getItemViewType(int position){
        Class c = mList.get(position).getClass();
        if (c.isInstance(IssueNameNode.class)){
            return NODE_TYPE.ISSUE_NAME.getInt();
        }else if(c.isInstance(StringNode.class))
            return NODE_TYPE.DESCRIPTION.getInt();
        else if (c.isInstance(BoolNode.class))
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
                        .inflate(R.layout.issue_name, parent,false);
                viewHolder = new ViewHolder(view, viewType);
                return viewHolder;
            case 2: // Issue DESCRIPTION
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.issue_status, parent, false);
                viewHolder = new ViewHolder(view, viewType);
                return viewHolder;
                default:
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.issue_status, parent, false);
                    viewHolder = new ViewHolder(view, viewType);
                    return viewHolder;
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case 1: // IssueName
                holder.mItem_description.setText("Issue name");
                holder.mItem_input.setText("Type issue name here!");
            default: // Issue Description
                    holder.mItem_description.setText("Default");
                holder.mItem_input.setText("Default text");

        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setTemplate(Template template){
        if (!mList.isEmpty())
        mList.clear();
        mList.addAll(template.getNodes());
    }

}
