package com.bimapp;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bimapp.controller.FragmentDashboard;
import com.bimapp.controller.FragmentNewTopic;
import com.bimapp.controller.FragmentProject;
import com.bimapp.controller.FragmentTopic;
import com.bimapp.controller.FragmentTopicList;
import com.bimapp.controller.interfaces.ProjectsFragmentInterface;
import com.bimapp.model.ImageFile;
import com.bimapp.model.data_access.DataProvider;
import com.bimapp.model.data_access.entityManagers.IssueBoardExtensionsEntityManager;
import com.bimapp.model.data_access.entityManagers.ProjectEntityManager;
import com.bimapp.model.data_access.network.APICall;
import com.bimapp.model.data_access.network.Callback;
import com.bimapp.model.data_access.network.NetworkConnManager;
import com.bimapp.model.entity.IssueBoardExtensions;
import com.bimapp.model.entity.Project;
import com.bimapp.model.entity.Template.Template;
import com.bimapp.model.entity.Topic;
import com.bimapp.model.entity.User;

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
        FragmentTopic.TopicFragmentListener {
    public final static String DASHBOARD_FRAGMENT_TAG = "fragment_dashboard";
    public final static String NEWTOPIC_FRAGMENT_TAG = "fragment_new_topic";
    public final static String TOPICLIST_FRAGMENT_TAG = "fragment_topics";
    public final static String PROJECTS_FRAGMENT_TAG = "fragment_projects";
    public final static String TOPIC_FRAGMENT_TAG = "fragment_topic";
    public final static String COMMENT_FRAGMENT_TAG = "fragment_comment";
    public static final String SYNC_TAG = "sync_manual";

    // Variables to select unique requests to other apps, and provides a way for this activity to handle those callbacks
    private final static int TAKE_PHOTO_INTENT = 91;


    private BimApp mApplication;
    private DrawerLayout mDrawerLayout;
    private User user;
    private Uri mImageUri;

    private Fragment mDashboardFragment;
    private FragmentNewTopic mNewTopicFragment;
    private FragmentTopicList mTopicListFragment;
    private Fragment mProjectsFragment;
    private FragmentTopic mTopicFragment;
    private Fragment mSyncFragment;
    private TextView toolbarProjectNameText;
    private NavigationView navigationView;
    private ActionBar mActionBar;

    // Variables for accounts
    // The authority for the sync adapter's content provider
    //public static final String AUTHORITY = "com.bimapp.model.data_access.DataProvider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.bimapp.sync";
    // The account name
    public static final String ACCOUNT = "BimAppSync";
    // Instance fields
    Account mAccount;

    /**
     * Used to manage the backstack Primarily.
     */
    final FragmentManager fragmentManager = ProjectsViewActivity.this.getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        mApplication = (BimApp) getApplication();

        mDashboardFragment = new FragmentDashboard();
        mTopicListFragment = new FragmentTopicList();
        mNewTopicFragment = new FragmentNewTopic();
        mProjectsFragment = new FragmentProject();
        mTopicFragment = new FragmentTopic();
        //mSyncFragment = new FragmentManualSync();

        toolbarProjectNameText = findViewById(R.id.toolbar_project_text);

        if(mApplication.checkLogIn()) {
        mAccount = CreateSyncAccount(this.getApplicationContext());
/*
        // Code for testing sync-adapter
        String authority = getString(R.string.data_provider_authority);
        String account_type = getString(R.string.sync_account_type);
        String account = "default_account";
        Account mAccount = new Account(account,account_type);
        AccountManager am = (AccountManager) mApplication.getSystemService(ACCOUNT_SERVICE);
        if (am.addAccountExplicitly(mAccount,null,null))
            Log.d("Account", "Account added");
        else
            Log.d("Account", "Didn't add account");
*/
            Bundle b = new Bundle();
            b.putBoolean(
                    ContentResolver.SYNC_EXTRAS_MANUAL, true);
            b.putBoolean(
                    ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            ContentResolver.setSyncAutomatically(mAccount, DataProvider.AUTHORITY, true);
        }
        //ContentResolver.requestSync(mAccount,DataProvider.AUTHORITY,b);

        // End of code for sync-adapter testing



        if (mApplication.checkLogIn())
            NetworkConnManager.networkRequest(mApplication, Request.Method.GET,
                    APICall.GETUser(), this, null);

    }

    @Override
    public void onSaveInstanceState(Bundle b) {
        super.onSaveInstanceState(b);
    }

    @Override
    public void onResume() {
        super.onResume();




        //Setting toolbar as the actionbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // Defines the drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);
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
                                openFragment(PROJECTS_FRAGMENT_TAG);
                                break;
                            case R.id.nav_issues:
                                mTopicListFragment.setTopicsAssignedTo(null);
                                openFragment(TOPICLIST_FRAGMENT_TAG);
                                break;
                            case R.id.nav_dashboard:
                                openFragment(DASHBOARD_FRAGMENT_TAG);
                                break;
                            case R.id.nav_log_out:
                                mApplication.logOut();
                                Intent intent = new Intent(ProjectsViewActivity.this, WelcomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                break;
                            //   case R.id.nav_sync:
                            //     openFragment(SYNC_TAG);
                            //   break;
                        }
                        return true;
                    }
                }
        );

        if (mApplication.getActiveProject() == null)
            setInitialActiveProject();
        else {
            mActionBar.setTitle(mApplication.getActiveProject().getBimsyncProjectName());
            toolbarProjectNameText.setText(mApplication.getActiveProject().getName());
        }
        if (fragmentManager.getBackStackEntryCount() == 0)
            openFragment(DASHBOARD_FRAGMENT_TAG);



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
        if (response != null)
            Log.d("ProjectsViewActivity", response);
        if(mApplication.getCurrentUser() != null) {
            TextView textView = findViewById(R.id.nav_header_title);
            String headerString = mApplication.getCurrentUser().getName()
                    + "\n(" + mApplication.getCurrentUser().getId() + ")";
            textView.setText(headerString);
        }

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
        mApplication.setCurrentUser(user);
        String headerString = mApplication.getCurrentUser().getName()
                + "\n(" + mApplication.getCurrentUser().getId() + ")";
        textView.setText(headerString);
    }

    /**
     * User clicked on a project in the projects fragment.
     *
     * @param project that was clicked on.
     */
    @Override
    public void onFragmentProjectInteraction(Project project) {
        navigationView.setCheckedItem(R.id.nav_dashboard);
        openFragment(DASHBOARD_FRAGMENT_TAG);
        toolbarProjectNameText.setText(project.getName());
        mActionBar.setTitle(project.getBimsyncProjectName());

    }

    /**
     * User clicked on a template in the dashboard Fragment.
     *
     * @param template that was clicked on.
     */
    @Override
    public void onDashboardItemClick(Template template) {
        if(template.getAssignedTo() == null) {
            mNewTopicFragment = new FragmentNewTopic();
            mNewTopicFragment.setTemplate(template);
            navigationView.setCheckedItem(0);
            openFragment(NEWTOPIC_FRAGMENT_TAG);
        }else{
            mTopicListFragment.setTopicsAssignedTo(user.getId());
            navigationView.setCheckedItem(R.id.nav_issues);
            openFragment(TOPICLIST_FRAGMENT_TAG);

        }

    }

    @Override
    public void onTopicSelected(Topic topic) {
        FragmentTopic.setTopic(topic);
        navigationView.setCheckedItem(0);
        openFragment(TOPIC_FRAGMENT_TAG);
    }


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
     *
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
        openFragment(DASHBOARD_FRAGMENT_TAG);
    }

    @Override
    public void onTakePhoto() {

        // TODO HANDLE USER DENYING ACCESS TO CAMERA!!!


        if (ContextCompat.checkSelfPermission(mApplication, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 123);
        } else {

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
    }

    @Override
    public void onFragmentFinish() {
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
        String tag = DASHBOARD_FRAGMENT_TAG;
        if (backStackEntryCount > 1) {
            tag = fragmentManager.getBackStackEntryAt
                    (backStackEntryCount - 2).getName();
        }
        openFragment(tag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if (requestCode == TAKE_PHOTO_INTENT) {
            if (resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    Uri imageUri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                sendImageTOFragment(bitmap);


            }
        }
    }

    private void sendImageTOFragment(Bitmap bitmap) {
        String tag = fragmentManager.getBackStackEntryAt
                (fragmentManager.getBackStackEntryCount() - 1).getName();
        switch (tag) {
            case (NEWTOPIC_FRAGMENT_TAG):
                mNewTopicFragment.setImage(bitmap);
                break;
            case (TOPIC_FRAGMENT_TAG):
                mTopicFragment.setImage(bitmap);
                break;
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
                    mExtensionManager.getIssueBoardExtensions(projects.get(0), new IssueBoardExtensionsEntityManager.IssueBoardExtensionsProjectCallback() {
                        @Override
                        public void setExtensions(IssueBoardExtensions issueBoardExtensions) {
                            projects.get(0).setIssueBoardExtensions(issueBoardExtensions);
                            mApplication.setActiveProject(projects.get(0));
                            toolbarProjectNameText.setText(projects.get(0).getName());
                            mActionBar.setTitle(projects.get(0).getBimsyncProjectName());
                        }
                    });

                }
            }
        });
    }

    /**
     * tag is one of the public static final String variables belonging
     * to this class.
     *
     * @param tag a String connected to a certain fragment.
     */
    public void openFragment(String tag) {
        switch (tag) {
            case (DASHBOARD_FRAGMENT_TAG):
                openFragment(mDashboardFragment, tag);
                break;
            case (TOPIC_FRAGMENT_TAG):
                openFragment(mTopicFragment, tag);
                break;
            case (NEWTOPIC_FRAGMENT_TAG):
                openFragment(mNewTopicFragment, tag);
                break;
            case (TOPICLIST_FRAGMENT_TAG):
                openFragment(mTopicListFragment, tag);
                mTopicListFragment.loadAllTopics();
                break;
            case (PROJECTS_FRAGMENT_TAG):
                openFragment(mProjectsFragment, tag);
                break;
            case (SYNC_TAG):
                openFragment(mSyncFragment, tag);
                break;
        }
    }

    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            Log.d("Account", "Added new account");
            ContentResolver.setIsSyncable(newAccount, DataProvider.AUTHORITY, 1);
            ContentResolver.setSyncAutomatically(newAccount, DataProvider.AUTHORITY, true);
            return newAccount;
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
            Log.d("Account", "Not added account");
            newAccount = accountManager.getAccountsByType(ACCOUNT_TYPE)[0];
            return newAccount;
        }
    }
}

