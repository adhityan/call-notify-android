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

        if (stateString.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            String formattedNumber = incomingNumber
                    .replace("00971", "")
                    .replace("0971", "")
                    .replace("+971", "")
                    .replace("971", "")
                    .replace("+97", "")
                    .replace("+91", "")
                    .replace("091", "")
                    .replace("+", "")
                    .replace("(", "")
                    .replace(")", "")
                    .replace(" ", "");
            Utilities.logDebug("incomingNumber: " + incomingNumber);

            if(Utilities.isNumeric(formattedNumber)) {
                Utilities.startNotificationService(context, formattedNumber, incomingNumber, NumberSource.CALL);
            }
            else Utilities.logDebug("Number ignored");
        }
    }
}
