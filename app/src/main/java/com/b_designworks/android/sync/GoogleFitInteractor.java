package com.b_designworks.android.sync;

import android.support.annotation.NonNull;

import com.b_designworks.android.Api;
import com.b_designworks.android.login.models.UserResponse;
import com.b_designworks.android.utils.storage.IStorage;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Ilya Eremin on 9/13/16.
 */

public class GoogleFitInteractor {

    private static final String KEY_GOOGLE_FIT_AUTHORIZATION_ENABLED = "googleFitAuthorizationEnabled";

    @NonNull private final IStorage storage;
    @NonNull private final Api      api;

    @Inject
    public GoogleFitInteractor(@NonNull IStorage storage, @NonNull Api api) {
        this.storage = storage;
        this.api = api;
    }

    public Observable<UserResponse> sendGoogleCodeToServer(String serverAuthCode) {
        return api.integrateFitnessApp(serverAuthCode, Provider.GOOGLE_FIT);
    }

    public void setGoogleFitAuthorizationEnabled(boolean enabled) {
        storage.putBoolean(KEY_GOOGLE_FIT_AUTHORIZATION_ENABLED, enabled);
    }

    public boolean isGoogleFitAuthorizationEnabled() {
        return storage.getBoolean(KEY_GOOGLE_FIT_AUTHORIZATION_ENABLED, false);
    }
}
