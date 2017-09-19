package com.gamechange.adhityan.callspyplusplus.activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gamechange.adhityan.callspyplusplus.R;
import com.gamechange.adhityan.callspyplusplus.libs.Utilities;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.dummy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivityPermissionsDispatcher.someFunctionWithCheck(MainActivity.this);
                initPubnub();
            }
        });
    }

    private void initPubnub() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-c86701de-89a0-11e7-9aaf-aec3d745d57e");

        PubNub pubnub = new PubNub(pnConfiguration);
        pubnub.addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                Utilities.logDebug("PUBNUB Status: " + status.getOperation());

                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    // internet got lost, do some magic and call reconnect when ready
                    pubnub.reconnect();
                } else if (status.getCategory() == PNStatusCategory.PNTimeoutCategory) {
                    // do some magic and call reconnect when ready
                    pubnub.reconnect();
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) { }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                // Handle new message stored in message.message
                Utilities.logDebug("Message received");

                if (message.getChannel() != null) {
                    // Message has been received on channel group stored in
                    // message.getChannel()

                    JsonObject m = message.getMessage().getAsJsonObject();
                    String type = m.get("type").getAsString();

                    if(type.equalsIgnoreCase("CALL")) {
                        String phone = m.get("data").getAsJsonObject().get("phone").getAsString();
                        Utilities.logDebug("CALL PHONE: " + phone);

                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                        startActivity(intent);
                    }
                    else if(type.equalsIgnoreCase("SMS")) {
                        String phone = m.get("data").getAsJsonObject().get("phone").getAsString();
                        Utilities.logDebug("SMS PHONE: " + phone);

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phone, null, "Test", null, null);
                    }
                }
                /*
                else {
                    // Message has been received on channel stored in
                    // message.getSubscription()
                }

                    log the following items with your favorite logger
                        - message.getMessage()
                        - message.getSubscription()
                        - message.getTimetoken()
                */
            }
        });

        pubnub.subscribe().channels(Arrays.asList("alexa")).execute();
    }

    @NeedsPermission({ Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS })
    public void someFunction() {
        Log.d("ADI", "Permission obtained");
        Toast.makeText(this, "Complete!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
