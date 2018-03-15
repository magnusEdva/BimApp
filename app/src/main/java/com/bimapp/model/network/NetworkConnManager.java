package com.bimapp.model.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.telecom.Call;

import com.bimapp.BimApp;

/**
 * Created by zorri on 15/03/2018.
 */

public class NetworkConnManager {


    private NetworkConnManager() {
    }

    static public void GETrequest(@NonNull BimApp context, @NonNull JSONTypes responseType,
                                  @NonNull APICall call, @NonNull Callback callback) {
        GETRequest.GET(context, responseType, call, callback);

    }

    public enum JSONTypes {
        OBJECT, ARRAY;
    }

    public enum APICall {
        GETProjects("https://api.bimsync.com/v2/projects");

        private String mURL;

        APICall(String URL) {
            mURL = URL;
        }

        public String getURL() {
            return mURL;
        }
    }
}
