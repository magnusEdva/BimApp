package com.bimapp.model.data_access.entityManagers;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

import com.bimapp.controller.interfaces.TopicFragmentInterface;
import com.bimapp.model.data_access.AppDatabase;
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
        TopicFragmentInterface listener = null;
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
                Long dateAcquired = cursor.getLong(cursor.getColumnIndex(AppDatabase.DATE_COLUMN));
                AppDatabase.statusTypes localStatus = AppDatabase.convertStringToStatus
                        (cursor.getString(cursor.getColumnIndex(AppDatabase.STATUS_COLUMN)));
                comments.add(new Comment(guid, verbalStatus, status, Date, author, topic,
                        modifiedDate, modifiedAuthor, comment, viewpointGuid, dateAcquired, localStatus));
                if (viewpointGuid != null) {
                    Object[] arr = new Object[2];
                    arr[0] = listener;
                    arr[1] = comments.get(comments.size() - 1);
                    this.startQuery(2, arr, DataProvider.ParseUri(DataProvider.VIEWPOINT_TABLE), null,
                            guid, null, null);
                }
            }
            listener.setComments(comments);
            //getPictures
        } else if (token == 2) {
            Object[] array = null;
            Comment comment = null;
            if (cookie instanceof Object[]) {
                array = (Object[]) cookie;
                if (array[0] instanceof TopicFragmentInterface)
                    listener = (TopicFragmentInterface) array[0];
                if (array[1] instanceof Comment)
                    comment = (Comment) array[1];
            } else throw new UnsupportedOperationException();
            if(listener != null && comment != null)
            if (cursor.moveToFirst()) {
                String guid = cursor.getString(cursor.getColumnIndex(Viewpoint.GUID));
                String commentGUID = cursor.getString(cursor.getColumnIndex(Viewpoint.COMMENT_GUID));
                String type = cursor.getString(cursor.getColumnIndex("type"));
                String pictureName = cursor.getString(cursor.getColumnIndex("picture_name"));
                AppDatabase.statusTypes localStatus = AppDatabase.convertStringToStatus
                        (cursor.getString(cursor.getColumnIndex(AppDatabase.STATUS_COLUMN)));
                Long dateAcquired = cursor.getLong(cursor.getColumnIndex(AppDatabase.DATE_COLUMN));
                Viewpoint vp = new Viewpoint(guid, commentGUID, type, pictureName, dateAcquired, localStatus);
                comment.setViewpoint(vp);
                listener.editComment(comment);
            }
            

        }
    }
}
