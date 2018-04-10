package com.bimapp.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bimapp.R;
import com.bimapp.model.entity.Template.Template;
import com.bimapp.model.entity.Template.TemplateNode;
import com.bimapp.view.interfaces.NewTopicViewInterface;

import java.util.ArrayList;
import java.util.List;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {

    private Template mTemplate;
    private List<TemplateNode>  mList;
    private List<View> mViewList;
    private final NewTopicViewInterface mListener;

    /**
     * Provide a reference to the views for each item
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public final LinearLayout mLayout;
        public View mView;


        public final TextView mIssueName;
        public final EditText mIssueNameInput;


        public ViewHolder(View itemView) {
            super(itemView);
            mLayout = itemView.findViewById(R.id.newtopic_list);
            mView = itemView;
            mIssueName = itemView.findViewById(R.id.issue_name);
            mIssueNameInput = itemView.findViewById(R.id.issue_name_input);
        }
    }

    public TemplateAdapter (List<View> viewList, NewTopicViewInterface listener){
        mViewList = viewList;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            case 0:
        }
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.newtopic_list, parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mView = mViewList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setTemplate(Template template){
        mList.clear();
        mList.addAll(template.getNodes());
    }

}
