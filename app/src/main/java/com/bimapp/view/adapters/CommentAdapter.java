package com.bimapp.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bimapp.R;
import com.bimapp.model.entity.Comment;
import com.bimapp.view.interfaces.TopicViewInterface;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter used to show comments in a list in TopicView
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    /**
     * list with comments
     */
    private List<Comment> mComments;
    /**
     * for interactions
     */
    private TopicViewInterface mListener;

    public CommentAdapter(){
        mComments = new ArrayList<>();
    }

    public void setComments(List<Comment> comments){
        mComments.clear();
        mComments.addAll(comments);
    }

    public void addComment(Comment comment){
        mComments.add(comment);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_topic_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.ContentView.setText(mComments.get(position).getComment());
        holder.NameView.setText(mComments.get(position).getAuthor());
        holder.DateView.setText(mComments.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView NameView;
        public final TextView ContentView;
        public final TextView DateView;
        public final View ItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.ItemView = itemView;
            this.NameView = itemView.findViewById(R.id.TopicCommentName);
            this.DateView = itemView.findViewById(R.id.TopicCOmmentDate);
            this.ContentView = itemView.findViewById(R.id.TopicCommentContent);
        }
    }
}
