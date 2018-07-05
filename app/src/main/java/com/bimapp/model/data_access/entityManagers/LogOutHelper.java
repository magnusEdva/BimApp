package com.bimapp.model.data_access.entityManagers;

import android.os.AsyncTask;

import com.bimapp.model.data_access.AppDatabase;

public class LogOutHelper extends AsyncTask<Void,Void,Void> {
    AppDatabase mInstance;
    public LogOutHelper(AppDatabase instance){
        mInstance = instance;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        mInstance.clearAllTables();
        return null;
    }
}
