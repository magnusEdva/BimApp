package com.bimapp.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bimapp.R;
import com.bimapp.model.entity.Template.Template;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {

    Template mTemplate;

    /**
     * Provide a reference to the views for each item
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public String mTitle;
        private String mDescription;
        private Integer mColor;
        private Integer mIcon;

        // Holds Views, arranges them
        public ViewHolder(View itemView, Template template) {
            super(itemView);
        }
    }

    public TemplateAdapter (Template template){
        mTemplate = template;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TextView to display "Title", then PlainTextView to type in title, arranged horizontally. Add to ViewHolder

        // Then same with mDescription

        // Then same with mColor

        // Then same with mIcon

        // Finally return ViewHolder
        View v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_newtopic,parent,false);

        return new ViewHolder(v, mTemplate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
