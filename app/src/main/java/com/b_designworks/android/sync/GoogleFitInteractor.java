package com.b_designworks.android.sync;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.b_designworks.android.Api;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.login.models.UserResponse;
import com.google.android.gms.auth.api.Auth;

import javax.inject.Inject;

import rx.Observable;

import static com.b_designworks.android.sync.GoogleFitScreen.mClient;

/**
 * Created by Ilya Eremin on 9/13/16.
 */

public class GoogleFitInteractor {


    @NonNull private final Api api;
    @NonNull private final Context context;
    @NonNull private final UserInteractor userInteractor;

    @Inject
    public GoogleFitInteractor(@NonNull Api api, @NonNull Context context, @NonNull UserInteractor userInteractor) {
        this.api = api;
        this.context = context;
        this.userInteractor = userInteractor;
    }

    private void buildClient() {

    }

    public void requestGoogleCode(@NonNull Activity activity, @IntRange(from = 0, to = 9999) int requestCode) {
        buildClient();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mClient);
        activity.startActivityForResult(signInIntent, requestCode);
    }

    public Observable<UserResponse> sendGoogleCodeToServer(String serverAuthCode) {
        return api.sendSecretCode(userInteractor.getUserId(), "google", serverAuthCode);
    }
}
