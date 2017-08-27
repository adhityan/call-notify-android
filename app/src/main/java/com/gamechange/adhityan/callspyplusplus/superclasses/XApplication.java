package com.gamechange.adhityan.callspyplusplus.superclasses;

/**
 * Created by adhityan on 8/16/17.
 */

import android.app.Application;

import com.gamechange.adhityan.callspyplusplus.libs.api.APICall;

public class XApplication extends Application  {
    public static final String SHARED_PREFERENCES_NAME = "adhityan";
    public static final String API_HOST = "https://lady-alexa.herokuapp.com/";
    //public static final String API_HOST = "https://caller-feedback.herokuapp.com/";
    //public static final String API_HOST = "http://192.168.1.2/caller-feedback/api/";

    @Override
    public void onCreate() {
        super.onCreate();
        APICall.init();
    }
}
