package com.gamechange.adhityan.callspyplusplus.superclasses;

/**
 * Created by adhityan on 8/16/17.
 */

import android.app.Application;

import com.gamechange.adhityan.callspyplusplus.libs.api.APICall;

public class XApplication extends Application  {
    public static final String SHARED_PREFERENCES_NAME = "parapa";
    public static final String API_HOST = "https://api.nmbr.club/";
    //public static final String API_HOST = "http://192.168.1.2/zencard/api/";

    @Override
    public void onCreate() {
        super.onCreate();
        APICall.init(this);
    }
}
