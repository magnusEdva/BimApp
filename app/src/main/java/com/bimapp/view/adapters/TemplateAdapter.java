package com.bimapp.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bimapp.BimApp;
import com.bimapp.R;
import com.bimapp.model.entity.IssueBoardExtensions;
import com.bimapp.model.entity.Project;
import com.bimapp.model.entity.Template.Template;
import com.bimapp.model.entity.Template.TemplateNode;
import com.bimapp.model.entity.Template.defaultNode;
import com.bimapp.view.interfaces.NewTopicViewInterface;

import java.util.List;

/**
 * Class used by {@link com.bimapp.view.NewTopicView} to show the correct view for each item
 * in a template.
 */
public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {

    private final NewTopicViewInterface mListener;
    private Template mTemplate;
    private List<TemplateNode> mList;
    private BimApp mContext;
    private Project mActiveProject;
    private int mDefaultStatus;
    private int mDefaultType;
    private int mDefaultAsssignedTo;

    //private final NewTopicViewInterface mListener;
    private String mTitle;
    private String mDesription;
    private String mComment;
    private String mAssignedTo;
    private String mIssueType;
    private String mIssueStatus;

    public String getTitle() {
        return mTitle;
    }

    public String getDesription() {
        return mDesription;
    }

    public String getComment() {
        return mComment;
    }

    public String getAssignedTo() {
        return mAssignedTo;
    }

    public String getTopicType() {
        return mIssueType;
    }

    public String getTopicStatus() {
        return mIssueStatus;
    }

    /**
     * Enum to separate node types
     */
    private enum NODE_TYPE {
        TITLE(1),
        DESCRIPTION(2),
        TOPIC_STATUS(3),
        TOPIC_TYPE(4),
        ASSIGNED_TO(5),
        LABELS(6),
        DUE_DATE(7),
        IMAGE(8),
        COMMENT(9)
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
        public TextView mItem_description;
        public EditText mItem_input;
        public Spinner mSpinner_input;
        public ArrayAdapter<CharSequence> mAdapter;
        public Button mItem_button;

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
                    //this.mItem_description = itemView.findViewById(R.id.topic_title);
                    this.mItem_input = itemView.findViewById(R.id.topic_title_input);
                    this.mSpinner_input = null;
                    break;
                case 2: // DESCRIPTION
                    //this.mItem_description = itemView.findViewById(R.id.topic_description);
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
                case 6: // LABELS
                    mItem_description = itemView.findViewById(R.id.topic_labels);
                    mItem_input = null;
                    mSpinner_input =null;
                    break;
                case 7: // DUE_DATE
                    mItem_description = itemView.findViewById(R.id.topic_due_date);
                    mItem_input = itemView.findViewById(R.id.topic_due_date_input);
                    mSpinner_input = null;
                    break;
                case 8: // IMAGE
                    mItem_description =null;
                    mItem_input = null;
                    mSpinner_input = null;
                    mItem_button = itemView.findViewById(R.id.topic_image_button);
                    break;
                case 9: // COMMENT
                    //mItem_description = itemView.findViewById(R.id.topic_comment);
                    mItem_input = itemView.findViewById(R.id.topic_comment_input);
                    break;
                default: // Defaults to no view
                    this.mItem_description = itemView.findViewById(R.id.topic_default);
                    this.mItem_input = itemView.findViewById(R.id.topic_default_input);
                    this.mSpinner_input = null;
                    break;
            }
            mLayout = itemView.findViewById(R.id.newtopic_list);
            mView = itemView;
        }

        public List<String> reorderList(List<String> strings, String defaultValue){
            if(defaultValue == null || !strings.contains(defaultValue))
                return strings;
            strings.remove(defaultValue);
            strings.add(0,defaultValue);
            return strings;
        }

    }

    /**
     * Constructor
     * @param template the template this adapter should conform to
     * @param listener
     */
    public TemplateAdapter(Template template, NewTopicViewInterface listener) {
        mList =  template.getNodes();
        mContext = (BimApp) listener.getRootView().getContext().getApplicationContext();
        mActiveProject = mContext.getActiveProject();
        mTemplate = template;
        mListener = listener;
        setExtensionsDefaultValue();
    }

    /**
     * Checks the templateList for what view should be associated with that position
     *
     * @param position the position to find the viewType of
     * @return an int, from the corresponding ENUM
     */
    @Override
    public int getItemViewType(int position) {
        TemplateNode c = mList.get(position);
        if (c.getTitle().equals(Template.TITLE))
            return NODE_TYPE.TITLE.getInt();
        else if (c.getTitle().equals(Template.DESCRIPTION))
            return NODE_TYPE.DESCRIPTION.getInt();
        else if (c.getTitle().equals(Template.TOPIC_STATUS))
            return NODE_TYPE.TOPIC_STATUS.getInt();
        else if (c.getTitle().equals(Template.TOPIC_TYPE))
            return NODE_TYPE.TOPIC_TYPE.getInt();
        else if (c.getTitle().equals(Template.ASSIGNED_TO))
            return NODE_TYPE.ASSIGNED_TO.getInt();
        else if (c.getTitle().equals(Template.DUE_DATE))
            return NODE_TYPE.DUE_DATE.getInt();
        else if (c.getTitle().equals((Template.LABELS)))
            return NODE_TYPE.LABELS.getInt();
        else if (c.getTitle().equals(Template.IMAGE))
            return NODE_TYPE.IMAGE.getInt();
        else if (c.getTitle().equals(Template.COMMENT))
            return NODE_TYPE.COMMENT.getInt();
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
            case 6: // LABELS
                view = LayoutInflater.from(context).inflate(R.layout.topic_labels,parent,false);
                viewHolder = new ViewHolder(view, viewType, context);
                break;
            case 7: // DUE DATE
                view = LayoutInflater.from(context).inflate(R.layout.topic_due_date,parent,false);
                viewHolder = new ViewHolder(view, viewType, context);
                break;
            case 8: // IMAGE
                view = LayoutInflater.from(context).inflate(R.layout.topic_image,parent,false);
                viewHolder = new ViewHolder(view, viewType, context);
                break;
            case 9: // COMMENT
                view = LayoutInflater.from(context).inflate(R.layout.topic_comment,parent,false);
                viewHolder = new ViewHolder(view, viewType,context);
                break;
            default:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.topic_default, parent, false);
                viewHolder = new ViewHolder(view, viewType, context);
                break;
        }
        viewHolder.setIsRecyclable(false);
        return viewHolder;
    }


    /**
     * Method which binds the items in the view to resources
     * @param holder the ViewHolder which holds the view
     * @param position the position of this view in the RecyclerView, corresponds to the position in the data set
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        switch (this.getItemViewType(position)) {
            case 1: // IssueTitle
                //holder.mItem_description.setText(R.string.issue_name);
                holder.mItem_input.setText(mList.get(position).getContent().toString());
                holder.mItem_input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mTitle = s.toString();
                        mList.get(position).setContent(s.toString());
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                break;
            case 2: // IssueDescription
                //holder.mItem_description.setText(R.string.issue_description);
                holder.mItem_input.setText(mList.get(position).getContent().toString());
                holder.mItem_input.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        mDesription = s.toString();
                        mList.get(position).setContent(s.toString());
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                break;
            case 3: // IssueStatus
                holder.mItem_description.setText(R.string.issue_status);
                holder.mSpinner_input.setAdapter(holder.mAdapter);
                holder.mSpinner_input.setSelection(mDefaultStatus);
                mIssueStatus = (String) holder.mSpinner_input.getSelectedItem();
                holder.mSpinner_input.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mDefaultStatus = parent.getSelectedItemPosition();
                        mIssueStatus = (String) parent.getSelectedItem();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                //holder.mItem_input.setText(mList.get(position).getContent().toString());
                break;
            case 4: // TOPIC_TYPE
                holder.mItem_description.setText(R.string.topic_type);
                holder.mSpinner_input.setAdapter(holder.mAdapter);
                holder.mSpinner_input.setSelection(mDefaultType);
                mIssueType = (String) holder.mSpinner_input.getSelectedItem();
                holder.mSpinner_input.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mDefaultType = parent.getSelectedItemPosition();
                        mIssueType = (String) parent.getSelectedItem();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                //holder.mItem_input.setText(mList.get(position).getContent().toString());
                break;
            case 5: // Assigned to
                holder.mItem_description.setText(R.string.assigned_to);
                holder.mSpinner_input.setAdapter(holder.mAdapter);
                holder.mSpinner_input.setSelection(mDefaultAsssignedTo);
                mAssignedTo = (String) holder.mSpinner_input.getSelectedItem();
                holder.mSpinner_input.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mDefaultAsssignedTo = parent.getSelectedItemPosition();
                        mAssignedTo = (String) parent.getSelectedItem();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                //holder.mItem_input.setText(mList.get(position).getContent().toString());
                break;
            case 6: // LABELS
                holder.mItem_description.setText(R.string.topic_label);
                //holder.mItem_input.setText(mList.get(position).getContent().toString());
                break;
            case 7: // DUE DATE
                holder.mItem_description.setText(R.string.due_date);
                holder.mItem_input.setText(mList.get(position).getContent().toString());
                break;
            case 8: //IMAGE
                holder.mItem_button.setText(R.string.add_image);
                holder.mItem_button.setOnClickListener(mListener);
                break;
            case 9: // COMMENT
                //holder.mItem_description.setText("Comment");
                holder.mItem_input.setText(mList.get(position).getContent().toString());
                holder.mItem_input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mComment = s.toString();
                    mList.get(position).setContent(s.toString());
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
                break;
            default: // DEFAULT
                holder.mItem_description.setText(mTemplate.getNodes().get(position).getTitle());
                holder.mItem_input.setHint(mTemplate.getNodes().get(position).getTitle());
                holder.mItem_input.setText("");
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

    public void setExtensionsDefaultValue() {
        IssueBoardExtensions issueBoardExtensions = mActiveProject.getIssueBoardExtensions();
        List<TemplateNode> nodes = mTemplate.getNodes();



        for (TemplateNode n : nodes) {
            if (n.getTitle().equals("topic_status")) {
                defaultNode dn = (defaultNode) n;
                String content = (String) dn.getContent();
                if (!content.equals(""))
                    mDefaultStatus = issueBoardExtensions.getTopicStatus().indexOf(content);
                else
                    mDefaultStatus = issueBoardExtensions.getTopicStatus().indexOf("Open");
            } else if (n.getTitle().equals("topic_type")) {
                defaultNode dn = (defaultNode) n;
                String content = (String) dn.getContent();
                if (!content.equals(""))
                    mDefaultType = issueBoardExtensions.getTopicType().indexOf(content);
                else
                    mDefaultType = issueBoardExtensions.getTopicType().indexOf("Info");
            } else if (n.getTitle().equals("assigned_to")) {
                defaultNode dn = (defaultNode) n;
                String content = (String) dn.getContent();
                if (!content.equals("")) {
                    mDefaultAsssignedTo = issueBoardExtensions.getUserIdType().indexOf(content);
                }
            }
        }
        // Checks if index are out of bound (-1)
        if (mDefaultType < 0)
            mDefaultType = 0;
        if (mDefaultAsssignedTo < 0)
            mDefaultAsssignedTo = 0;
        if (mDefaultStatus < 0)
            mDefaultStatus = 0;

    }

}
