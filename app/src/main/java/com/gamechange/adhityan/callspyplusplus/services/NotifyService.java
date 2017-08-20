package com.gamechange.adhityan.callspyplusplus.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.gamechange.adhityan.callspyplusplus.libs.Utilities;
import com.gamechange.adhityan.callspyplusplus.libs.api.APICall;
import com.gamechange.adhityan.callspyplusplus.libs.api.apiInterface;
import com.gamechange.adhityan.callspyplusplus.superclasses.XApplication;


public class NotifyService extends IntentService implements apiInterface {
    public NotifyService()  {
        super("NotifyService");
        //setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String number = intent.getStringExtra("number");
        String unformattedNumber = intent.getStringExtra("unformattedNumber");
        String source = intent.getStringExtra("source");

        processNumber(number, unformattedNumber, source);
    }

    public void processNumber(String number, String unformattedNumber, String source) {
        Utilities.logDebug("Processing: " + number + ", " + unformattedNumber + ", " + source);
        new APICall(this, XApplication.API_HOST + "sms/" + number, "sms");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        APICall.init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void apiResponse(String response, String code) {
        Utilities.logDebug("Response: " + response);
    }

    @Override
    public void apiError(String message, String code) {
        Utilities.logError("Get phone error: " + message);
    }

    @Override
    public void connectionError(int Status, String code) {
        Utilities.logWarning("Connection failed.");
    }
}
