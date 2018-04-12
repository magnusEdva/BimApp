package com.bimapp.controller;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.NewTopicFragmentInterface;
import com.bimapp.model.entity.Template.Template;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.entityManagers.TopicsEntityManager;
import com.bimapp.view.NewTopicView;
import com.bimapp.view.interfaces.NewTopicViewInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentNewTopic.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FragmentNewTopic extends Fragment implements NewTopicViewInterface.NewTopicToPresenter, NewTopicFragmentInterface {




    public interface NewTopicFragmentInterface{
        interface FragmentNewTopicListener{
            void getTemplate(NewTopicFragmentInterface callback);
        }
        void setTemplate(Template template);
    }

    private NewTopicViewInterface mNewTopicView;
    private BimApp mApplication;

    // This is the listener for the activity
    private OnFragmentInteractionListener mListener;


    @Override
    public void onPostTopic(Topic topic) {
        TopicsEntityManager manager = new TopicsEntityManager(mApplication, this);
        Log.d("FragmentNewTopic", "Made an entityManger");
        manager.postTopic(this, topic);
    }


    public FragmentNewTopic() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mApplication = (BimApp) this.getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Instantiate the view

        // Templates for testing. Need to be passed from activity which template should be shown.
        MOCKTEMPLATES MOCK = new MOCKTEMPLATES();
        Template t = new Template(MOCK.templateOne);

        mNewTopicView = new NewTopicView(inflater,container, t);
        mNewTopicView.registerListener(this);
        // Inflate the layout for this fragment
        return mNewTopicView.getRootView();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onPostingTopic(boolean success);
    }

    /**
     * Override method from the interface {@link com.bimapp.controller.interfaces.NewTopicFragmentInterface}
     * Gets called when the {@link TopicsEntityManager} has finished posting a topic
     */
    @Override
    public void postedTopic(boolean success) {
        if (mListener != null)
            mListener.onPostingTopic(success);
    }

    /**
     * Class to create templates for testing purposes
     */
    public class MOCKTEMPLATES{
        JSONObject templateOne;

        JSONObject templateTwo;

        MOCKTEMPLATES(){
            try{
                templateTwo = new JSONObject();
                templateOne = new JSONObject();

                templateOne.put(Template.TITLE, "Test-template 1");
                templateOne.put(Template.DESCRIPTION, "Template to show concept");
                templateOne.put(Template.COLOR, Color.GREEN);
                templateOne.put(Template.ICON, 1);


                templateTwo.put(Template.TITLE, "New safety-inspection item");
                templateTwo.put(Template.DESCRIPTION, "Daily safety inspection onsite. Please document all issues with images and a description");
                templateTwo.put(Template.COLOR, Color.YELLOW);
                templateTwo.put(Template.ICON, 2);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
