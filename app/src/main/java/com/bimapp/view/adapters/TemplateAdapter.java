package com.bimapp.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bimapp.BimApp;
import com.bimapp.R;
import com.bimapp.model.entity.Template.TopicAssignedToNode;
import com.bimapp.model.entity.Template.TopicDescriptionNode;
import com.bimapp.model.entity.Template.TopicStatusNode;
import com.bimapp.model.entity.Template.TopicTitleNode;
import com.bimapp.model.entity.Template.Template;
import com.bimapp.model.entity.Template.TemplateNode;
import com.bimapp.model.entity.Template.TopicTypeNode;
import com.bimapp.view.interfaces.NewTopicViewInterface;

import java.util.List;

/**
 * Class used by {@link com.bimapp.view.NewTopicView} to show the correct view for each item
 * in a template.
 */
public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {

    private Template mTemplate;
    private List<TemplateNode> mList;

    //private final NewTopicViewInterface mListener;

    /**
     * Enum to separate node types
     */
    private enum NODE_TYPE {
        TITLE(1),
        DESCRIPTION(2),
        TOPIC_STATUS(3),
        TOPIC_TYPE(4),
        ASSIGNED_TO(5),
        ;

        private int i;

        NODE_TYPE(int i) {
            this.i = i;
        }

        public int getInt() {
            return i;
        }
    }

    /**
     * ViewHolder class which provide a reference to the views for each item. Used by this Adapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout mLayout;
        public View mView;
        public final TextView mItem_description;
        public final EditText mItem_input;
        public final Spinner mSpinner_input;
        public ArrayAdapter<CharSequence> mAdapter;

        /**
         * Constructor which makes a ViewHolder depending on what subtype the {@link TemplateNode} has
         * @param itemView the view which should be populated by this
         * @param viewType the viewType of the {@link TemplateNode}. See method getItemViewType for more info
         */
        public ViewHolder(View itemView, int viewType, Context context) {
            super(itemView);
            BimApp mContext = (BimApp) context.getApplicationContext();
            switch (viewType) {
                case 1: // TITLE
                    this.mItem_description = itemView.findViewById(R.id.topic_title);
                    this.mItem_input = itemView.findViewById(R.id.topic_title_input);
                    this.mSpinner_input = null;
                    break;
                case 2: // DESCRIPTION
                    this.mItem_description = itemView.findViewById(R.id.topic_description);
                    this.mItem_input = itemView.findViewById(R.id.topic_description_input);
                    this.mSpinner_input = null;
                    break;
                case 3: // TOPIC_STATUS
                    mAdapter = new ArrayAdapter(context
                            , R.layout.support_simple_spinner_dropdown_item
                            , mContext.getActiveProject().getIssueBoardExtensions().getTopicStatus());
                    this.mItem_description = itemView.findViewById(R.id.topic_status);
                    this.mItem_input = null;
                    this.mSpinner_input = itemView.findViewById(R.id.topic_status_input);
                    break;
                case 4: // TOPIC_TYPE
                    mAdapter = new ArrayAdapter (context
                            , R.layout.support_simple_spinner_dropdown_item
                            , mContext.getActiveProject().getIssueBoardExtensions().getTopicType());

                    this.mItem_description = itemView.findViewById(R.id.topic_type);
                    this.mSpinner_input = itemView.findViewById(R.id.topic_type_input);
                    this.mItem_input = null;
                    break;
                case 5: // ASSIGNED_TO
                    mAdapter = new ArrayAdapter(context
                            , R.layout.support_simple_spinner_dropdown_item
                            , mContext.getActiveProject().getIssueBoardExtensions().getUserIdType());
                    this.mItem_description = itemView.findViewById(R.id.topic_assigned_to);
                    this.mItem_input = null;
                    this.mSpinner_input = itemView.findViewById(R.id.topic_assigned_to_input);
                    break;
                default: // Defaults to no view
                    this.mItem_description = itemView.findViewById(R.id.topic_status);
                    this.mItem_input = itemView.findViewById(R.id.topic_status_input);
                    this.mSpinner_input = null;
                    break;
            }
            mLayout = itemView.findViewById(R.id.newtopic_list);
            mView = itemView;
        }
    }

    /**
     * Constructor
     * @param template the template this adapter should conform to
     * @param listener
     */
    public TemplateAdapter(Template template, NewTopicViewInterface listener) {
        mList =  template.getNodes();//new ArrayList<>();

        // These aren't used?
        //mList.addAll(template.getNodes());
        //mListener = listener;
    }

    /**
     * Checks the templateList for what view should be associated with that position
     *
     * @param position the position to find the viewType of
     * @return an int, from the corresponding ENUM
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

    /**
     * Method used by the manager(?.. or Adapter)to create a ViewHolder of the correct type
     * with the correct layout inflated.
     * @param parent the parent, which the view presented by this ViewHolder is a child of
     * @param viewType the type of the view, see ENUM in this class
     * @return a Viewholder which holds the view for this type.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        ViewHolder viewHolder;
        Context context = parent.getContext();


        switch (viewType) {
            case 1: // TITLE
                view = LayoutInflater.from(context)
                        .inflate(R.layout.topic_title, parent, false);
                viewHolder = new ViewHolder(view, viewType, context);
                break;
            case 2: // DESCRIPTION
                view = LayoutInflater.from(context)
                        .inflate(R.layout.topic_description, parent, false);
                viewHolder = new ViewHolder(view, viewType,context);
                break;
            case 3: // TOPIC_STATUS
                view = LayoutInflater.from(context)
                        .inflate(R.layout.topic_status,parent,false);
                viewHolder = new ViewHolder(view, viewType, context);
                break;
            case 4: // TOPIC_TYPE
                view = LayoutInflater.from(context)
                        .inflate(R.layout.topic_type,parent,false);
                viewHolder = new ViewHolder(view, viewType,context);
                break;
            case 5: // ASSIGNED_TO
                view = LayoutInflater.from(context).inflate(R.layout.topic_assignedto, parent, false);
                viewHolder = new ViewHolder(view, viewType, context);
                break;
            default:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.topic_status, parent, false);
                viewHolder = new ViewHolder(view, viewType, context);
                break;
        }
        return viewHolder;
    }


    /**
     * Method which binds the items in the view to resources
     * @param holder the ViewHolder which holds the view
     * @param position the position of this view in the RecyclerView, corresponds to the position in the data set
     */
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
                holder.mSpinner_input.setAdapter(holder.mAdapter);
                //holder.mItem_input.setText(mList.get(position).getContent().toString());
                break;
            case 4: // TOPIC_TYPE
                holder.mItem_description.setText(R.string.topice_type);
                holder.mSpinner_input.setAdapter(holder.mAdapter);
                //holder.mItem_input.setText(mList.get(position).getContent().toString());
                break;
            case 5: // Assigned to
                holder.mItem_description.setText(R.string.assigned_to);
                holder.mSpinner_input.setAdapter(holder.mAdapter);
                //holder.mItem_input.setText(mList.get(position).getContent().toString());
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
