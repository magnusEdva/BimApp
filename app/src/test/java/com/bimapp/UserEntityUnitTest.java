package com.bimapp;

import com.bimapp.model.entity.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the USER entity class.
 */

public class UserEntityUnitTest {

    @Mock
    private JSONObject mTestJSON;

    private String id = "testId";
    private String name = "testName";

    @Before
    public void setup() {
        mTestJSON = mock(JSONObject.class);
        try {
            when(mTestJSON.getString(User.NAME)).thenReturn(name);
            when(mTestJSON.getString(User.ID)).thenReturn(id);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testJsonConstructor(){
        User testUser = new User(mTestJSON);

        assertEquals(testUser.getId(), id);
        assertEquals(testUser.getName(),name);
    }
}
