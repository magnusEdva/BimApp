package com.bimapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bimapp.model.oauth.OAuthHandler;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Håkon on 09.03.2018.
 */

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void login_button(View view){
        String url = "https://api.bimsync.com/1.0/oauth/authorize?client_id=RoQ3h7VtoKkAMnS&redirect_uri=bimapp://oauthresponse&state=1&response_type=code";
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    @Override
    public void onNewIntent(Intent intent){
        Uri uri = intent.getData();

        if (uri != null && uri.toString().startsWith("bimapp://oauthresponse")){
            String code = uri.getQueryParameter("code");

            BimApp context = (BimApp) getApplication();
            context.setAuthorizationCode(code);
            Intent launchLoggedin = new Intent(this.getApplicationContext(), LoggedIn.class);
            startActivity(launchLoggedin);
        }
    }
}
