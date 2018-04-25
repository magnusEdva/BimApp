package com.bimapp.view.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bimapp.R;
import com.bimapp.model.entity.Comment;
import com.bimapp.view.interfaces.TopicViewInterface;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Bitmap.createScaledBitmap;

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
    /**
     * Reference to class variable used to blow up an image.
     */
    private ImageView mFullscreenImage;

    public CommentAdapter(ImageView fullscreenImage) {
        mFullscreenImage = fullscreenImage;
        mComments = new ArrayList<>();
    }

    public void setComments(List<Comment> comments) {
        mComments = comments;
        notifyDataSetChanged();
    }

    public void addComment(Comment comment) {
        mComments.add(comment);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_topic_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.ContentView.setText(mComments.get(position).getComment());
        holder.NameView.setText(mComments.get(position).getAuthor());
        holder.DateView.setText(mComments.get(position).getDate());

        if (mComments.get(position).getViewpoint() != null && mComments.get(position).getViewpoint().getSnapshot() != null) {
            holder.bitmap = mComments.get(position).getViewpoint().getSnapshot();
            holder.imageView.setImageBitmap(scaleDown(holder.bitmap, 500, false));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFullscreenImage.setVisibility(View.VISIBLE);
                    mFullscreenImage.setImageBitmap(holder.bitmap);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView NameView;
        public final TextView ContentView;
        public final TextView DateView;
        public final ImageView imageView;
        public final View ItemView;
        public Bitmap bitmap;

        public ViewHolder(View itemView) {
            super(itemView);
            this.ItemView = itemView;
            this.NameView = itemView.findViewById(R.id.TopicCommentName);
            this.DateView = itemView.findViewById(R.id.TopicCOmmentDate);
            this.imageView = itemView.findViewById(R.id.TopicCommentImage);
            this.ContentView = itemView.findViewById(R.id.TopicCommentContent);
        }
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}
