package com.pairup.android.chat.pushes;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.pairup.android.R;
import com.pairup.android.UserInteractor;
import com.pairup.android.chat.ChatScreen;
import com.pairup.android.utils.di.Injector;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import javax.inject.Inject;

/**
 * Created by Ilya Eremin on 10/4/16.
 */

public class PushNotificationsHandlerService extends FirebaseMessagingService {

    private static final String TAG = "PushNotificationsHandle";

    @Inject Gson mapper;
    @Inject UserInteractor userInteractor;

    @Override public void onCreate() {
        super.onCreate();
        Injector.inject(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(userInteractor.isNotificationsEnabled()) {
            String jsonMessage = remoteMessage.getData().get("message");
            PushMessage pushMessage = mapper.fromJson(jsonMessage, PushMessage.class);

            Intent intent = new Intent(this, ChatScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(pushMessage.getName())
                .setContentText(pushMessage.getText())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }

}