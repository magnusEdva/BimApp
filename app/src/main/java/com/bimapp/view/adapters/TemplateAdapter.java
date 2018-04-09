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

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public TemplateAdapter (Template template){
        mTemplate = template;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_newtopic,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return 0;
    }

}
