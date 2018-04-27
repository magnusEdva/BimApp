package com.bimapp.model.data_access.entityManagers;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

import com.bimapp.controller.interfaces.TopicFragmentInterface;
import com.bimapp.model.data_access.DataProvider;
import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Viewpoint;

import java.util.ArrayList;
import java.util.List;

public class CommentDBHandler extends AsyncQueryHandler {
    public CommentDBHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        TopicFragmentInterface listener;
        List<Comment> comments = new ArrayList<>();
        //token == 1 means get Comments command
        if (token == 1) {
            if (cookie instanceof TopicFragmentInterface)
                listener = (TopicFragmentInterface) cookie;
            else throw new UnsupportedOperationException();

            while (cursor.moveToNext()) {
                String guid = cursor.getString(cursor.getColumnIndex(Comment.GUID));
                String verbalStatus = cursor.getString(cursor.getColumnIndex(Comment.VERBAL_STATUS));
                String status = cursor.getString(cursor.getColumnIndex(Comment.STATUS));
                String Date = cursor.getString(cursor.getColumnIndex(Comment.DATE));
                String author = cursor.getString(cursor.getColumnIndex(Comment.AUTHOR));
                String topic = cursor.getString(cursor.getColumnIndex(Comment.TOPIC_GUID));
                String modifiedDate = cursor.getString(cursor.getColumnIndex(Comment.MODIFIED_DATE));
                String modifiedAuthor = cursor.getString(cursor.getColumnIndex(Comment.MODIFIED_AUTHOR));
                String comment = cursor.getString(cursor.getColumnIndex(Comment.COMMENT));
                String viewpointGuid = cursor.getString(cursor.getColumnIndex(Comment.VIEWPOINT_GUID));
                comments.add(new Comment(guid, verbalStatus, status, Date, author, topic, modifiedDate, modifiedAuthor, comment, viewpointGuid));
                if (viewpointGuid != null) {
                    this.startQuery(2, listener, DataProvider.ParseUri(DataProvider.VIEWPOINT_TABLE), null,
                            viewpointGuid, null, null);
                }
            }
            listener.setComments(comments);
            //getPictures
        } else if (token == 2) {
            if (cookie instanceof TopicFragmentInterface)
                listener = (TopicFragmentInterface) cookie;
            else throw new UnsupportedOperationException();
            if(cursor.moveToFirst()) {
                String guid = cursor.getString(cursor.getColumnIndex(Viewpoint.GUID));
                String commentGUID = cursor.getString(cursor.getColumnIndex(Viewpoint.COMMENT_GUID));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String pictureName = cursor.getString(cursor.getColumnIndex("picture_name"));
                Viewpoint vp = new Viewpoint(guid, commentGUID, type, pictureName);
            }
        }
    }
}
