package com.bimapp;

import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //https://developer.chrome.com/apps/identity#method-launchWebAuthFlow
        String url = "https://paul.kinlan.me/";
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
                //context.setAuthorizationCode(code);
            }
        }
    }

