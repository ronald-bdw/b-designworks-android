package com.b_designworks.android.sync;

import android.support.annotation.NonNull;

import com.b_designworks.android.Api;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.login.models.UserResponse;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Ilya Eremin on 9/13/16.
 */

public class GoogleFitInteractor {

    @NonNull private final Api api;
    @NonNull private final UserInteractor userInteractor;

    @Inject
    public GoogleFitInteractor(@NonNull Api api, @NonNull UserInteractor userInteractor) {
        this.api = api;
        this.userInteractor = userInteractor;
    }

    public Observable<UserResponse> sendGoogleCodeToServer(String serverAuthCode) {
        return api.sendSecretCode(userInteractor.getUserId(), "google", serverAuthCode);
    }
}
