package com.bimapp.fragmentsold;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.bimapp.BimApp;
import com.bimapp.R;
import com.bimapp.model.entity.Project;
import com.bimapp.model.network.Callback;
import com.bimapp.model.network.NetworkConnManager;
import com.bimapp.fragmentsold.ProjectsFragment.OnListFragmentInteractionListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Project} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyProjectsRecyclerViewAdapter extends RecyclerView.Adapter<MyProjectsRecyclerViewAdapter.ViewHolder>
    implements Callback {

    private final List<Project> mProjects;
    private final OnListFragmentInteractionListener mListener;
    private final BimApp mContext;

    public MyProjectsRecyclerViewAdapter(List<Project> projects, OnListFragmentInteractionListener listener, Context context) {
        mProjects = projects;
        mListener = listener;
        mContext = (BimApp) context.getApplicationContext();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_projects, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mProject = mProjects.get(position);
        holder.mContentView.setText(mProjects.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mProject);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }

    public void loadProjects(){
        NetworkConnManager.networkRequest(mContext, Request.Method.GET,NetworkConnManager.JSONTypes.ARRAY,
                NetworkConnManager.APICall.GETProjects, this, null);
    }
    @Override
    public void onError(String response) {
        //TODO
    }

    @Override
    public void onSuccess(JSONArray arr) {
        Project p = new Project();
        mProjects.clear();
        mProjects.addAll(Arrays.asList((Project[])p.construct(arr)));
        this.notifyDataSetChanged();
        Log.d("got here", mProjects.get(0).toString());
    }

    @Override
    public void onSuccess(JSONObject obj) {
        //??
        Log.d("obj", "project");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public Project mProject;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
