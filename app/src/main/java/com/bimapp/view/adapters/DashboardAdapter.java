package com.bimapp.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bimapp.R;
import com.bimapp.model.entity.Template.Template;
import com.bimapp.view.interfaces.DashboardViewInterface;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Template} and makes a call to the
 * specified {@link }.
 * TODO: Replace the implementation with code for your data type.
 */
public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private final List<Template> mValues;
    private final DashboardViewInterface mListener;

    public DashboardAdapter(List<Template> items, DashboardViewInterface listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.template = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getTitle());
        holder.mContentView.setText(mValues.get(position).getDescription());
        holder.mLayout.setBackgroundColor(holder.template.getColor());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onInteraction(holder.template);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final LinearLayout mLayout;
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Template template;

        public ViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.dashboard_list_main_layout);
            mView = view;
            mIdView = view.findViewById(R.id.id);
            mContentView = view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public void setTemplates(List<Template> templates){
        mValues.clear();
        mValues.addAll(templates);
    }
}
