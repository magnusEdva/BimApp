package com.bimapp.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.BimApp;
import com.bimapp.controller.interfaces.NewTopicFragmentInterface;
import com.bimapp.model.data_access.entityManagers.TopicsEntityManager;
import com.bimapp.model.entity.Comment;
import com.bimapp.model.entity.Template.Template;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.entity.Viewpoint;
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
public class FragmentNewTopic extends Fragment implements NewTopicViewInterface.NewTopicToPresenter,
        NewTopicFragmentInterface {


    private NewTopicViewInterface mNewTopicView;
    private BimApp mApplication;

    private Template mTemplate;

    // This is the listener for the EntityManager
    private OnFragmentInteractionListener mListener;
    private Bitmap mImage;
    // This is the listener for the activity


    @Override
    public void onPostTopic(Topic topic, Comment comment, Viewpoint vp) {
        TopicsEntityManager manager = new TopicsEntityManager(mApplication, this);
        Log.d("FragmentNewTopic", "Made an entityManger");
        manager.postTopic(this, topic, comment, vp);
    }

    @Override
    public void onCameraIntent(View view) {
        mListener.onTakePhoto();
    }


    public FragmentNewTopic() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mApplication = (BimApp) this.getActivity().getApplication();
        // Templates for testing. Need to be passed from activity which template should be shown.
        //MOCKTEMPLATES MOCK = new MOCKTEMPLATES();
        //mTemplate = new Template(MOCK.templateOne);
    }

    @Override
    public void onResume(){
        super.onResume();
        mNewTopicView.updateExtensionsDefaultValue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (mNewTopicView == null) {

            mNewTopicView = new NewTopicView(inflater, container, mTemplate);
            mNewTopicView.registerListener(this);
        }
        // Inflate the layout for this fragment
        return mNewTopicView.getRootView();
    }


    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("Thing", "Other thing");
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

    public void setTemplate(Template template) {
        mTemplate = template;
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

        void onTakePhoto();

        void onFragmentFinish();
    }

    /**
     * Override method from the interface {@link com.bimapp.controller.interfaces.NewTopicFragmentInterface}
     * Gets called when the {@link TopicsEntityManager} has finished posting a topic
     */
    @Override
    public void postedTopic(boolean success, Topic topic) {
        /*if (success && topic != null){
            mNewTopicView.postedTopic(topic);
        } else
            mNewTopicView.postedTopic(topic);
        //if (mListener != null)
        // mListener.onPostingTopic(success);*/
    }

    public void setImage(Bitmap image){
        //mImage = image;
        mNewTopicView.setImage(image);
    }
    /**
     * Class to create templates for testing purposes
     */
    public class MOCKTEMPLATES {
        JSONObject templateOne;

        JSONObject templateTwo;

        MOCKTEMPLATES() {
            try {
                templateTwo = new JSONObject("{\n" +
                        "    \"templateName\": \"Uønsket hendelse\",\n" +
                        "    \"templateDescription\": \"\",\n" +
                        "    \"templateColor\": \"#f45f41\",\n" +
                        "    \"title\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": null\n" +
                        "    },\n" +
                        "    \"topic_status\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": \"Open\"\n" +
                        "    },\n" +
                        "    \"description\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": \"Rapport ved uønsket hendelse\"\n" +
                        "    },\n" +
                        "    \"topic_type\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": \"Incident\"\n" +
                        "    },\n" +
                        "    \"labels\": {\n" +
                        "        \"mandatory\": false,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": [\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"assigned_to\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": false,\n" +
                        "        \"defaultValue\": \"chief@company.com\"\n" +
                        "    },\n" +
                        "    \"stage\": {\n" +
                        "        \"mandatory\": false,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": \"Bygge fase 1\"\n" +
                        "    },\n" +
                        "    \"due_date\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": false,\n" +
                        "        \"defaultValue\": \"2018-12-05T00:00:00+01:00\"\n" +
                        "    },\n" +
                        "    \"image\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true\n" +
                        "    },\n" +
                        "    \"comment\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": null\n" +
                        "    }\n" +
                        "}");
                templateOne = new JSONObject("\n" +
                        "{\n" +
                        "    \"templateName\": \"Inspeksjonsrunde\",\n" +
                        "    \"templateDescription\": \"test description\",\n" +
                        "    \"templateColor\": \"#4286f4\",\n" +
                        "    \"title\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": null\n" +
                        "    },\n" +
                        "    \"description\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": \"Sjekkliste inspeksjonsrunde\"\n" +
                        "    },\n" +
                        "    \"topic_status\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": false,\n" +
                        "        \"defaultValue\": \"Open\"\n" +
                        "    },\n" +
                        "    \"topic_type\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": false,\n" +
                        "        \"defaultValue\": \"Inspection\"\n" +
                        "    },\n" +
                        "    \"labels\": {\n" +
                        "        \"mandatory\": false,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": [\n" +
                        "            \"Tidlig fase\",\n" +
                        "            \"Byggesone 1\"\n" +
                        "        ]\n" +
                        "    },\n" +
                        "    \"assigned_to\": {\n" +
                        "        \"mandatory\": false,\n" +
                        "        \"visible\": false,\n" +
                        "        \"defaultValue\": mlyngsedva@gmail.com\n" +
                        "    },\n" +

                        "    \"due_date\": {\n" +
                        "        \"mandatory\": false,\n" +
                        "        \"visible\": false,\n" +
                        "        \"defaultValue\": null\n" +
                        "    },\n" +
                        "    \"image\": {\n" +
                        "        \"mandatory\": true,\n" +
                        "        \"visible\": true\n" +
                        "    },\n" +
                        "    \"comment\": {\n" +
                        "        \"mandatory\": false,\n" +
                        "        \"visible\": true,\n" +
                        "        \"defaultValue\": null\n" +
                        "    }\n" +
                        "}");


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
