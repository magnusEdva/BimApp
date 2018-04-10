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

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {

    Template mTemplate;

    /**
     * Provide a reference to the views for each item
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public String mTitle;
        public TextView mTitleText;
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
        Log.d("Got", "here first");
        LinearLayout linearLayout = parent.findViewById(R.id.newtopic_layout);
        // TextView to display "Title", then PlainTextView to type in title, arranged horizontally. Add to ViewHolder
        TextView title = parent.findViewById(R.id.newtopic_title);
        title.setText("Title");
        EditText title_actual = parent.findViewById(R.id.newtopic_actual_title);

        linearLayout.addView(title);
        linearLayout.addView(title_actual);
        // Then same with mDescription

        // Then same with mColor

        // Then same with mIcon

        // Finally return ViewHolder
        
        return new ViewHolder(linearLayout, mTemplate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.d("Got", "here");
        holder.mTitleText.setText(mTemplate.getTitle());


    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
