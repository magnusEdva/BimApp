package com.bimapp.model.network;

import com.bimapp.model.entity.Project;
import com.bimapp.model.entity.Topic;

/**
 * Class used to store all supported URLS. It manages URL dependencies aswell.
 */

public class APICall {

    private static final String HOST_URL = "https://bcf.bimsync.com/bcf/";

    private static final String BASE_HOST_URL = "https://bimsync.com/projects/";

    private static final String VERSION_NUMBER = "2.1";



    private APICall(){}

    private static String BuildBcfURL(){
        return HOST_URL + VERSION_NUMBER;
    }

    public static String GETProjects(){
        return BuildBcfURL() + "/projects";
    }

    public static String GETUser(){
        return BuildBcfURL() + "/current-user";
    }



    /**
     *
     * @param project from which the Topics are to be acquired
     * @return completed String to be used in class NetworkConnManager
     */
    public static String GETTopics(Project project){
        return BuildBcfURL() + "/projects/" + project.getProjectId() + "/topics";
    }
    public static String POSTTopics(Project project){
        return BuildBcfURL() + "/projects/" + project.getProjectId() + "/topics";
    }
    public static String GETComments(Project project, Topic topic){
        return BuildBcfURL() + "/projects/" + project.getProjectId() + "/topics/" + topic.getGuid() + "/comments";
    }
    public static String POSTComment(Project project,Topic topic){
        return BuildBcfURL() + "/projects/" + project.getProjectId() + "/topics/" + topic.getGuid() + "/comments";
    }
    public static String GETIssueBoardExtensions(Project project) {
        return (BuildBcfURL() + "/projects/" + project.getProjectId() + "/extensions");
    }

}
