package com.pairup.android.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.pairup.android.UserInteractor;
import com.pairup.android.utils.Bus;
import com.pairup.android.utils.Logger;
import com.pairup.android.utils.di.Injector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

/**
 * Created by Ilya Eremin on 18.08.2016.
 */
public class SmsListener extends BroadcastReceiver {

    public static final String KEY_PHRASE = "PearUp security code: ";

    @Inject UserInteractor userInteractor;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Injector.inject(this, context);
            Bundle bundle = intent.getExtras();
            Logger.i("in message section");
            SmsMessage[] messages;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    messages = new SmsMessage[pdus.length];
                    String wholeString = "";
                    for (int i = 0; i < messages.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        wholeString += messages[i].getMessageBody();
                    }
                    if (wholeString.contains(KEY_PHRASE)) {
                        Pattern pattern = Pattern.compile(" [0-9]+.");
                        final Matcher matcher = pattern.matcher(wholeString);
                        if (matcher.find()) {
                            String str = matcher.group(0);
                            if (str.length() >= 3) {
                                String code = str.substring(1, str.length() - 1);
                                Logger.i("code is: " + code);
                                if (!userInteractor.userLoggedIn()) {
                                    Bus.event(new SmsCodeEvent(code), Bus.STICKY);
                                }
                            }
                        }
                        Logger.i("received message: " + wholeString);
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }

}