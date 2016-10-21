package com.b_designworks.android.sync;

import android.support.annotation.NonNull;

import com.b_designworks.android.Api;
import com.b_designworks.android.login.models.FitToken;
import com.b_designworks.android.login.models.UserResponse;
import com.b_designworks.android.utils.storage.IStorage;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by Ilya Eremin on 9/13/16.
 */

public class GoogleFitInteractor {

    @NonNull private final IStorage storage;
    @NonNull private final Api      api;

    @Inject
    public GoogleFitInteractor(@NonNull IStorage storage, @NonNull Api api) {
        this.storage = storage;
        this.api = api;
    }

    public Observable<FitToken> deleteGoogleTokenFromServer(String id){
        return api.deleteFitnessToken(id);
    }
}
