package com.gamechange.adhityan.callspyplusplus.libs;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gamechange.adhityan.callspyplusplus.services.NotifyService;
import com.gamechange.adhityan.callspyplusplus.structs.NumberSource;

/**
 * Created by adhityan on 8/16/17.
 */

public class Utilities {
    public static String logTag = "ECALLER";

    public static void logInfo(String message) {
        logInfo(logTag, message);
    }

    public static void logInfo(String tag, String message) {
        Log.i(tag, message);
        //Crashlytics.log(Log.INFO, tag, message);
    }

    public static void logDebug(String message) {
        logDebug(logTag, message);
    }

    public static void logDebug(String tag, String message) {
        Log.d(tag, message);
        //Crashlytics.log(Log.DEBUG, tag, message);
    }

    public static void logWarning(String message) {
        logWarning(logTag, message);
    }

    public static void logWarning(String tag, String message) {
        Log.w(tag, message);
        //Crashlytics.log(Log.WARN, tag, message);
    }

    public static void logError(String message) {
        logError(logTag, message);
    }

    public static void logError(String tag, String message) {
        Log.e(tag, message);
        //Crashlytics.log(Log.ERROR, tag, message);
    }

    public static void logError(String message, Exception e) {
        logError(logTag, message, e);
    }

    public static void logError(String tag, String message, Exception e) {
        Log.e(tag, message, e);
        //Crashlytics.log(Log.ERROR, tag, message);
        //Crashlytics.logException(e);
    }

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static void startNotificationService(Context context, String number, String unformattedNumber, NumberSource source) {
        Intent i = new Intent(context, NotifyService.class);
        i.putExtra("number", number);
        i.putExtra("unformattedNumber", unformattedNumber);
        i.putExtra("source", source.toString());
        context.startService(i);
    }
}
