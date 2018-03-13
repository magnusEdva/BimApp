package com.bimapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class LoggedIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        Uri uri = getIntent().getData();

        if (uri != null && uri.toString().startsWith("bimapp://oauthresponse")) {
            storeCode(uri.getQueryParameter("code"));
        }

    }

    private void storeCode(String code) {
        BimApp context = (BimApp) this.getApplicationContext();
        context.setAuthorizationCode(code);
    }


}
