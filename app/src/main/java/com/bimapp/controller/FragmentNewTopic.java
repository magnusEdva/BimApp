package com.bimapp.controller;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bimapp.BimApp;
import com.bimapp.model.entity.Template.Template;
import com.bimapp.model.entity.Template.TemplateNode;
import com.bimapp.model.entity.Topic;
import com.bimapp.view.NewTopicView;
import com.bimapp.view.interfaces.NewTopicViewInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentNewTopic.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FragmentNewTopic extends Fragment implements NewTopicViewInterface.NewTopicToPresenter{

    public interface NewTopicFragmentInterface{
        interface FragmentNewTopicListener{
            void getTemplate(NewTopicFragmentInterface callback);
        }
        void setTemplate(Template template);
    }

    private NewTopicViewInterface mNewTopicView;
    private BimApp mApplication;
    private OnFragmentInteractionListener mListener;


    @Override
    public void onPostTopic(Topic topic) {

    }


    public FragmentNewTopic() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // mApplication = (BimApp) this.getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Instantiate the view

        MOCKTEMPLATES MOCK = new MOCKTEMPLATES();
        Template t = new Template(MOCK.templateOne);
        List<View> viewList = createNodeViews(t);
        mNewTopicView = new NewTopicView(inflater,container, viewList);
        mNewTopicView.registerListener(this);
        // Inflate the layout for this fragment
        mNewTopicView.makeNewTopic(t);
        return mNewTopicView.getRootView();
    }

    /**
     * Creates a list of views from a list of TemplateNodes
     * @param template The template containing the nodes
     * @return A list of views made from nodes
     * //TODO Make method to show all
     */
    private List<View> createNodeViews(Template template){
        List<View> viewList = new ArrayList<>();
        for (TemplateNode tn: template.getNodes()) {
            tn.makeView(this.getContext());
            if (tn.isVisible())
                viewList.add(tn.getView());
        }
        return viewList;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class MOCKTEMPLATES{
        JSONObject templateOne;

        JSONObject templateTwo;

        public  MOCKTEMPLATES(){
            try{
                templateTwo = new JSONObject();
                templateOne = new JSONObject();

                templateOne.put(Template.TITLE, "title");
                templateOne.put(Template.DESCRIPTION, "description BLA BLA BLA BLA LBA BLA BLA BLA");
                templateOne.put(Template.COLOR, Color.GREEN);
                templateOne.put(Template.ICON, 1);


                templateTwo.put(Template.TITLE, "Title Two");
                templateTwo.put(Template.DESCRIPTION, "description Two BLA BLA BLA BLA LBA BLA BLA BLA");
                templateTwo.put(Template.COLOR, Color.RED);
                templateTwo.put(Template.ICON, 2);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
