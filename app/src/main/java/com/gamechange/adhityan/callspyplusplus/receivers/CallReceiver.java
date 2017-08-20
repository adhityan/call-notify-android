package com.gamechange.adhityan.callspyplusplus.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.gamechange.adhityan.callspyplusplus.libs.Utilities;
import com.gamechange.adhityan.callspyplusplus.structs.NumberSource;

/**
 * Created by adhityan on 8/16/17.
 */
public class CallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        // If, the received action is not a type of "Phone_State", ignore it
        if (!intent.getAction().equals("android.intent.action.PHONE_STATE"))
            return;

        String stateString = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        Utilities.logDebug("onCallStateChanged: " + stateString);

        if (stateString.equals(TelephonyManager.EXTRA_STATE_OFFHOOK) || stateString.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Utilities.logDebug("incomingNumber: " + incomingNumber);

            String formattedNumber = incomingNumber
                    .replace("+971", "")
                    .replace("+91", "")
                    .replace("+", "")
                    .replace("(", "")
                    .replace(")", "")
                    .replace(" ", "")
                    .replaceAll("^[0]+", "")
                    .replaceAll("^[0971]+", "")
                    .replaceAll("^[971]+", "")
                    .replaceAll("^[091]+", "")
                    .replaceAll("^[91]+", "");
            Utilities.logDebug("formattedNumber: " + formattedNumber);

            if(stateString.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                if (Utilities.isNumeric(formattedNumber)) {
                    Utilities.startNotificationService(context, formattedNumber, incomingNumber, NumberSource.CALL);
                } else Utilities.logDebug("Number ignored");
            }
        }
    }
}
