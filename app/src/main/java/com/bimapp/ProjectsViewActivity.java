package com.bimapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bimapp.controller.FragmentDashboard;
import com.bimapp.controller.FragmentNewComment;
import com.bimapp.controller.FragmentNewTopic;
import com.bimapp.controller.FragmentProject;
import com.bimapp.controller.FragmentTopic;
import com.bimapp.controller.FragmentTopicList;
import com.bimapp.controller.interfaces.ProjectsFragmentInterface;
import com.bimapp.model.ImageFile;
import com.bimapp.model.entity.IssueBoardExtensions;
import com.bimapp.model.entity.Project;
import com.bimapp.model.entity.Template.Template;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.entity.User;
import com.bimapp.model.entityManagers.IssueBoardExtensionsEntityManager;
import com.bimapp.model.entityManagers.ProjectEntityManager;
import com.bimapp.model.network.APICall;
import com.bimapp.model.network.Callback;
import com.bimapp.model.network.NetworkConnManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;


public class ProjectsViewActivity extends AppCompatActivity
        implements
        Callback<String>,
        FragmentProject.OnFragmentProjectInteractionListener,
        FragmentDashboard.DashboardListener,
        FragmentTopicList.TopicSelectionInterface,
        FragmentNewTopic.OnFragmentInteractionListener,
        FragmentTopic.TopicFragmentListener
{
    public final static String DASHBOARD_FRAGMENT_TAG = "fragment_dashboard";
    public final static String NEWTOPIC_FRAGMENT_TAG = "fragment_new_topic";
    public final static String TOPICLIST_FRAGMENT_TAG = "fragment_topics";
    public final static String PROJECTS_FRAGMENT_TAG = "fragment_projects";
    public final static String TOPIC_FRAGMENT_TAG = "fragment_topic";
    public final static String COMMENT_FRAGMENT_TAG = "fragment_comment";

    // Variables to select unique requests to other apps, and provides a way for this activity to handle those callbacks
    private final static int TAKE_PHOTO_INTENT = 91;

    private BimApp mApplication;
    private DrawerLayout mDrawerLayout;
    private User user;
    private Uri mImageUri;

    private Fragment mDashboardFragment;
    private FragmentNewTopic mNewTopicFragment;
    private Fragment mTopicListFragment;
    private Fragment mProjectsFragment;
    private Fragment mTopicFragment;
    private Fragment mNewCommentFragment;

    private int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        setContentView(R.layout.activity_logged_in);
        mApplication = (BimApp) getApplication();
        //This is a terrible way to handle things! It essentially gets the
        //user and sets the user variable through a callback method
        // Should be moved to some BimApp setting on login

        NetworkConnManager.networkRequest(mApplication, Request.Method.GET,
                APICall.GETUser(), this, null);

        if (mApplication.getActiveProject() == null)
            setInitialActiveProject();

        mDashboardFragment = new FragmentDashboard();
        mTopicListFragment = FragmentTopicList.newInstance(this);
        mNewTopicFragment = new FragmentNewTopic();
        mProjectsFragment = new FragmentProject();
        mTopicFragment = new FragmentTopic();
        mNewCommentFragment = new FragmentNewComment();
    }
    @Override
    public void onSaveInstanceState(Bundle b){
        super.onSaveInstanceState(b);
    }
    @Override
    public void onResume() {
        super.onResume();

        //Setting toolbar as the actionbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);


        // Defines the drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // set item as selected to persist highlight
                        //item.setChecked(true);
                        // Close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        int id = item.getItemId();

                        switch (id) {
                            case R.id.nav_projects:
                                openFragment(mProjectsFragment, PROJECTS_FRAGMENT_TAG);
                                break;
                            case R.id.nav_issues:
                                openFragment(mTopicListFragment, TOPICLIST_FRAGMENT_TAG);
                                break;
                            case R.id.nav_dashboard:
                                openFragment(mDashboardFragment, DASHBOARD_FRAGMENT_TAG);
                                break;
                            case R.id.nav_new_topic:
                                openFragment(mNewTopicFragment, NEWTOPIC_FRAGMENT_TAG);
                                break;
                            case R.id.nav_log_out:
                                mApplication.logOut();
                                Intent intent = new Intent(ProjectsViewActivity.this, WelcomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                break;
                        }
                        return true;
                    }
                }
        );
        if(state == 1)
            openFragment(mNewTopicFragment, NEWTOPIC_FRAGMENT_TAG);
        else
        openFragment(mDashboardFragment, DASHBOARD_FRAGMENT_TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onError(String response) {
        if(response != null)
        Log.d("ProjectsViewActivity", response);
    }

    /**
     * Network callback for fetching a user.
     *
     * @param response String in a JSON format containing a user.
     */
    @Override
    public void onSuccess(String response) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        user = new User(obj);
        Log.d("Created user", user.getName());
        TextView textView = findViewById(R.id.nav_header_title);
        textView.setText(user.getName());
    }

    /**
     * User clicked on a project in the projects fragment.
     *
     * @param project that was clicked on.
     */
    @Override
    public void onFragmentProjectInteraction(Project project) {
        state = 0;
        openFragment(mDashboardFragment, DASHBOARD_FRAGMENT_TAG);

    }

    /**
     * User clicked on a template in the dashboard Fragment.
     *
     * @param template that was clicked on.
     */
    @Override
    public void onDashboardItemClick(Template template) {
        state = 1;
        openFragment(mNewTopicFragment, NEWTOPIC_FRAGMENT_TAG);
    }

    @Override
    public void onTopicSelected(Topic topic) {
        state = 0;
        FragmentTopic.setTopic(topic);
        openFragment(mTopicFragment, TOPIC_FRAGMENT_TAG);
    }

    @Override
    public void openCommentFragment(Topic topic) {
        state = 0;
        FragmentNewComment.setTopic(topic);
        openFragment(mNewCommentFragment, COMMENT_FRAGMENT_TAG);
    }

    /**
     * only to be used in openFragment
     */
    final FragmentManager fragmentManager = ProjectsViewActivity.this.getSupportFragmentManager();

    /**
     * responsible for opening all the fragments this activity possess
     *
     * @param fragment instantiated Fragment to be opened
     */
    public void openFragment(Fragment fragment, String name) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction = backStackManager(name, fragmentTransaction);

        fragmentTransaction.replace(R.id.fragments_container, fragment, name);


        fragmentTransaction.commit();
    }

    /**
     * clears the Fragment back stack all the way to the top.
     */
    public void clearBackStack() {
        fragmentManager.popBackStack(null, POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * clears up to and including the Fragment in the backstack with Tag tag
     *
     * @param tag to be cleared up to an inclusive
     */
    public void clearBackStackInclusive(String tag) {
        fragmentManager.popBackStack(tag, POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * clears up to the Fragment in the backstack with Tag tag
     *
     * @param tag the tag to be popped.
     */
    public void clearBackStackExclusive(String tag) {
        fragmentManager.popBackStack(tag, 0);
    }

    /**
     * Manages the Fragment Backstack
     * @param tag
     * @param transaction
     * @return
     */
    public FragmentTransaction backStackManager(String tag, FragmentTransaction transaction) {
        switch (tag) {
            case DASHBOARD_FRAGMENT_TAG:
                clearBackStack();
                break;
            default:
                if (fragmentManager.findFragmentByTag(tag) == null) {
                    transaction.addToBackStack(tag);
                } else
                    clearBackStackExclusive(tag);
                break;
        }
        return transaction;
    }


    @Override
    public void onPostingTopic(boolean success) {

        if (success) {
            Toast.makeText(mApplication, "Successfully posted topic", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mApplication, "Didn't post topic", Toast.LENGTH_SHORT).show();
        }
        openFragment(mDashboardFragment, DASHBOARD_FRAGMENT_TAG);
    }

    @Override
    public void onTakePhoto(View v) {

        // TODO HANDLE USER DENYING ACCESS TO CAMERA!!!


        if(ContextCompat.checkSelfPermission(mApplication, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[] {Manifest.permission.CAMERA}, 123);
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = ImageFile.createImageFile(mApplication);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.bimapp.fileprovider",
                        photoFile);
                mImageUri = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, TAKE_PHOTO_INTENT);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == TAKE_PHOTO_INTENT) {
            if (resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    Uri imageUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // Add code to push bitmap to fragment here
                Bundle b = new Bundle();
                b.putCharSequence("uri", mImageUri.toString());
                mNewCommentFragment.setArguments(b);

                openFragment(mNewTopicFragment, NEWTOPIC_FRAGMENT_TAG);
            }
        }
    }


    public void setInitialActiveProject() {
        ProjectEntityManager projectEntityManager = new ProjectEntityManager(mApplication);
        projectEntityManager.getProjects(new ProjectsFragmentInterface() {
            @Override
            public void setProjects(final List<Project> projects) {
                if (projects != null && !projects.isEmpty()) {
                    IssueBoardExtensionsEntityManager mExtensionManager
                            = new IssueBoardExtensionsEntityManager(mApplication);
                    mExtensionManager.getIssueBoardExtensions(projects.get(0),new IssueBoardExtensionsEntityManager.IssueBoardExtensionsProjectCallback(){
                        @Override
                        public void setExtensions(IssueBoardExtensions issueBoardExtensions) {
                            projects.get(0).setIssueBoardExtensions(issueBoardExtensions);
                            mApplication.setActiveProject(projects.get(0));
                        }
                    });

                }
            }
        });
    }


}
