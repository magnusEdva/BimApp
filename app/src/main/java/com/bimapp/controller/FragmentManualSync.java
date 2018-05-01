package com.bimapp.controller;

import android.os.Bundle;
import android.view.View;

public class FragmentManualSync extends android.support.v4.app.Fragment {

    View mSyncView;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        mSyncView = new View(this.getContext());
    }
}
