package com.bimapp.model.network;

import com.bimapp.model.entity.Project;
import com.bimapp.model.entity.Topic;

/**
 * Created by zorri on 21/03/2018.
 */

public class APICall {

    private static final String HOST_URL = "https://bcf.bimsync.com/bcf/";

    private static final String VERSION_NUMBER = "2.1";



    private APICall(){}

    private static String BuildBaseURL(){
        return HOST_URL + VERSION_NUMBER;
    }

    public static String GETProjects(){
        return BuildBaseURL() + "/projects";
    }

    public static String GETUser(){
        return BuildBaseURL() + "/current-user";
    }

    /**
     *
     * @param project from which the Topics are to be acquired
     * @return completed String to be used in class NetworkConnManager
     */
    public static String GETTopics(Project project){
        return BuildBaseURL() + "/projects/" + project.getProjectId() + "/topics";
    }
    public static String POSTTopics(Project project){
        return BuildBaseURL() + "/projects/" + project.getProjectId() + "/topics";
    }
    public static String GETComments(Project project, Topic topic){
        return BuildBaseURL() + "/projects/" + project.getProjectId() + "/topics/" + topic.getmGuid() + "/comments";
    }

}
