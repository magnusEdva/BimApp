package com.bimapp;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.bimapp.model.entity.Project;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests on the Project Entity Type.
 */
public class ProjectEntityUnitTest {
    @Mock
    private JSONObject projectJSON;
    private Project project;

    private String projectId = "75749b87-2b51-4a99-ac21-a621e98137f7";
    private String name = "BCF";
    private String bimsyncProjectId = "bb76d10d62c24bc18dda452e5d0fe6be";
    private String bimsyncProjectName = "BachelorOppgave";


    @Before
    public void setup(){
        projectJSON = mock(JSONObject.class);
        try {
            when(projectJSON.getString(Project.PROJECT_ID)).thenReturn(projectId);
            when(projectJSON.getString(Project.NAME)).thenReturn(name);
            when(projectJSON.getString(Project.BIMSYNC_PROJECT_NAME)).thenReturn(bimsyncProjectName);
            when(projectJSON.getString(Project.BIMSYNC_PROJECT_ID)).thenReturn(bimsyncProjectId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @After
    public void reset(){}

    @Test
    public void JsonConstructorWorks() throws Exception {
        project = new Project(projectJSON);

        assertEquals(projectId, project.getProjectId());
        assertEquals(name, project.getName());
        assertEquals(bimsyncProjectId, project.getBimsyncProjectId());
        assertEquals(bimsyncProjectName, project.getBimsyncProjectName());
    }

    @Test
    public void StringConstructorWorks() throws Exception{
        project = new Project(projectId, bimsyncProjectName, bimsyncProjectId, name);

        assertEquals(projectId, project.getProjectId());
        assertEquals(name, project.getName());
        assertEquals(bimsyncProjectId, project.getBimsyncProjectId());
        assertEquals(bimsyncProjectName, project.getBimsyncProjectName());
    }
}