package com.pairup.android.chat.pushes;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Ilya Eremin on 10/4/16.
 */

public class PushInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "PushInstanceIdService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
}
