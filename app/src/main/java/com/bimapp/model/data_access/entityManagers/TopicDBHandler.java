package com.bimapp.model.data_access.entityManagers;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

import com.bimapp.controller.interfaces.TopicsFragmentInterface;
import com.bimapp.model.data_access.AppDatabase;
import com.bimapp.model.entity.Project;
import com.bimapp.model.entity.Topic;

import java.util.ArrayList;
import java.util.List;

public class TopicDBHandler extends AsyncQueryHandler {

    public TopicDBHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);
        if (token == 1 && cookie instanceof TopicsFragmentInterface && cursor != null) {
            getTopics((TopicsFragmentInterface) cookie, cursor);
        }
    }

    private void getTopics(TopicsFragmentInterface mListener, Cursor cursor) {
        List<Topic> topics = new ArrayList<>();
        while (cursor.moveToNext()) {
            String guid = cursor.getString(cursor.getColumnIndex(Topic.GUID));
            String description = cursor.getString(cursor.getColumnIndex(Topic.DESCRIPTION));
            int index = cursor.getInt(cursor.getColumnIndex(Topic.INDEX));
            String title = cursor.getString(cursor.getColumnIndex(Topic.TITLE));
            String dueDate = cursor.getString(cursor.getColumnIndex(Topic.DUE_DATE));
            String TopicType = cursor.getString(cursor.getColumnIndex(Topic.TOPIC_TYPE));
            String Status = cursor.getString(cursor.getColumnIndex(Topic.TOPIC_STATUS));
            List<String> Labels = Topic.getListFromString(cursor.getString(cursor.getColumnIndex(Topic.LABELS)));
            List<String> references = Topic.getListFromString(cursor.getString(cursor.getColumnIndex(Topic.REFERENCE_LINKS)));
            String modAuth = cursor.getString(cursor.getColumnIndex(Topic.MODIFIED_AUTHOR));
            String Stage = cursor.getString(cursor.getColumnIndex(Topic.STAGE));

            String mSnippet_type = cursor.getString(cursor.getColumnIndex(Topic.BimSnippet.SNIPPET_TYPE));

            String mReference = cursor.getString(cursor.getColumnIndex(Topic.BimSnippet.REFERENCE));

            String mReferenceSchema = cursor.getString(cursor.getColumnIndex(Topic.BimSnippet.REFERENCE_SCHEMA));

            boolean isExternal = cursor.getInt(cursor.getColumnIndex(Topic.BimSnippet.IS_EXTERNAL)) != 0;

            Topic.BimSnippet snippet = new Topic.BimSnippet(mSnippet_type, mReference, mReferenceSchema, isExternal);

            String priority = cursor.getString(cursor.getColumnIndex(Topic.PRIORITY));
            String creationAuthor = cursor.getString(cursor.getColumnIndex(Topic.CREATION_AUTHOR));
            String CreationDate = cursor.getString(cursor.getColumnIndex(Topic.CREATION_DATE));
            String AssignedTo = cursor.getString(cursor.getColumnIndex(Topic.ASSIGNED_TO));
            String projectId = cursor.getString(cursor.getColumnIndex(Project.PROJECT_ID));

            String statusColumn = cursor.getString(cursor.getColumnIndex(AppDatabase.DATE_COLUMN));
            Long dateAcquired = cursor.getLong(cursor.getColumnIndex(AppDatabase.STATUS_COLUMN));

            Topic topic = new Topic();
            topic.setTitle(title);
            topic.setTopicType(TopicType);
            topic.setTopicStatus(Status);
            topic.setAssignedTo(AssignedTo);
            topic.setDescription(description);
            topic.setBimSnippet(snippet);
            topic.setCreationAuthor(creationAuthor);
            topic.setGuid(guid);
            topic.setLabels(Labels);
            topic.setIndex(index);
            topic.setReferenceLinks(references);
            topic.setDueDate(dueDate);
            topic.setModifiedAuthor(modAuth);
            topic.setStage(Stage);
            topic.setPriority(priority);
            topic.setCreationDate(CreationDate);
            topic.setProjectId(projectId);
            topic.setDateAcquired(dateAcquired);
            topic.setLocalStatus(AppDatabase.convertStringToStatus(statusColumn));
            topics.add(topic);
        }
        mListener.setTopics(topics);
    }
}
