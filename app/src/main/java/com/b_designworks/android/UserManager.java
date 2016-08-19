package com.b_designworks.android;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.b_designworks.android.login.models.AuthResponse;
import com.b_designworks.android.login.models.RegisterResponse;
import com.b_designworks.android.utils.storage.IStorage;

import rx.Observable;
import rx.functions.Func1;


/**
 * Created by Ilya Eremin on 15.08.2016.
 */
public class UserManager {

    private static volatile UserManager instance;

    public static UserManager getInstance(@NonNull IStorage storage, @NonNull Api api) {
        UserManager localInstance = instance;
        if (localInstance == null) {
            synchronized (UserManager.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UserManager(storage, api);
                }
            }
        }
        return localInstance;
    }

    private static final String KEY_PHONE         = "phone";
    private static final String KEY_PHONE_CODE_ID = "phoneCodeId";
    private static final String KEY_TOKEN         = "token";
    private static final String KEY_USER_ID       = "userId";

    @NonNull private final IStorage storage;
    @NonNull private final Api      api;

    private UserManager(@NonNull IStorage storage, @NonNull Api api) {
        this.storage = storage;
        this.api = api;
    }

    public Observable<AuthResponse> requestCode(@NonNull String phone) {
        return api.sendMeCode(phone).map(result -> {
            storage.putString(KEY_PHONE, phone);
            storage.putString(KEY_PHONE_CODE_ID, result.getPhoneCodeId());
            return result;
        });
    }

    public Observable<Void> register(@NonNull String firstName, @NonNull String lastName,
                                     @NonNull String email, @NonNull String code) {
        return api.register(firstName, lastName, email, code, storage.getString(KEY_PHONE), storage.getString(KEY_PHONE_CODE_ID))
            .map(saveToken());
    }

    @NonNull private Func1<RegisterResponse, Void> saveToken() {
        return result -> {
            storage.remove(KEY_PHONE);
            storage.remove(KEY_PHONE_CODE_ID);
            storage.putString(KEY_TOKEN, result.getToken());
            storage.putString(KEY_USER_ID, result.getId());
            return null;
        };
    }

    @Nullable public String getToken() {
        return storage.getString(KEY_TOKEN);
    }

    public Observable<Void> verifyCode(@NonNull String verificationCode) {
        return api.signIn(verificationCode, storage.getString(KEY_PHONE), storage.getString(KEY_PHONE_CODE_ID))
            .map(saveToken());
    }

    public String getUserId() {
        return storage.getString(KEY_USER_ID);
    }
}
