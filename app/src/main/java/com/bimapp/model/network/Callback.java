package com.bimapp.model.network;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by zorri on 14/03/2018.
 */

public interface Callback {
    void onError(String response);
    void onSuccess(String JSONResponse);
}
