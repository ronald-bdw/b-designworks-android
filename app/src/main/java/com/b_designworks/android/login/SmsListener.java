package com.b_designworks.android.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.b_designworks.android.utils.Bus;

/**
 * Created by Ilya Eremin on 18.08.2016.
 */
public class SmsListener extends BroadcastReceiver {

    public static final String KEY_PHRASE = "top_secret_phrase";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] messages;
            if (bundle != null) {
                try {
                    if (Build.VERSION.SDK_INT >= 19) {
                        messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                    } else {
                        Object pdus[] = (Object[]) bundle.get("pdus");
                        messages = new SmsMessage[pdus.length];
                    }
                    for (SmsMessage msg : messages) {
//                        String msg_from = msg.getOriginatingAddress();
                        String msgBody = msg.getMessageBody();
                        // TODO user real parser when it will be done
                        if (msgBody != null && msgBody.contains(KEY_PHRASE)) {
                            Bus.event(new SmsCodeEvent(msgBody), Bus.STICKY);
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }

}
