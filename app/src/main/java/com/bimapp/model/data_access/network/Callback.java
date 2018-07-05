package com.bimapp.model.data_access.network;

/**
 * Created by zorri on 14/03/2018.
 */

public interface Callback<T> {
    void onError(String response);
    void onSuccess(T response);
}
