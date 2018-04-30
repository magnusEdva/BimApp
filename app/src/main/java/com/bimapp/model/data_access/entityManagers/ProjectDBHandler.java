package com.bimapp.model.data_access.entityManagers;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;

import com.bimapp.controller.interfaces.ProjectsFragmentInterface;
import com.bimapp.model.entity.IssueBoardExtensions;
import com.bimapp.model.entity.Project;
import com.bimapp.model.entity.Topic;

import java.util.ArrayList;
import java.util.List;

public class ProjectDBHandler extends AsyncQueryHandler {
    public ProjectDBHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        if (token == 1) {
            if(cookie instanceof ProjectsFragmentInterface && cursor != null)
                getProjects((ProjectsFragmentInterface) cookie, cursor);
        }
    }


    public void getProjects(ProjectsFragmentInterface controllerCallback, Cursor cursor) {
        List<Project> projects = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(Project.NAME));
            String projectId = cursor.getString(cursor.getColumnIndex(Project.PROJECT_ID));
            String bimsyncProjectID = cursor.getString(cursor.getColumnIndex(Project.BIMSYNC_PROJECT_ID));
            String bimsyncProjectName = cursor.getString(cursor.getColumnIndex(Project.BIMSYNC_PROJECT_NAME));
            String labels = cursor.getString(cursor.getColumnIndex(IssueBoardExtensions.TOPIC_LABELS));
            String status = cursor.getString(cursor.getColumnIndex(IssueBoardExtensions.TOPIC_STATUS));
            String type = cursor.getString(cursor.getColumnIndex(IssueBoardExtensions.TOPIC_TYPE));
            String used_id = cursor.getString(cursor.getColumnIndex(IssueBoardExtensions.USER_ID_TYPE));
            IssueBoardExtensions extensions = new IssueBoardExtensions(Topic.getListFromString(type),Topic.getListFromString(status),
                    Topic.getListFromString(used_id),Topic.getListFromString(labels));
            Project project = new Project(projectId,bimsyncProjectName,bimsyncProjectID,name, extensions);
            projects.add(project);
        }
        controllerCallback.setProjects(projects);

    }
}
