package com.b_designworks.android;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.b_designworks.android.login.models.AuthResponse;
import com.b_designworks.android.login.models.User;
import com.b_designworks.android.login.models.UserResponse;
import com.b_designworks.android.utils.storage.IStorage;

import rx.Observable;
import rx.functions.Func1;


/**
 * Created by Ilya Eremin on 15.08.2016.
 */
public class UserInteractor {


    private static volatile UserInteractor instance;

    public static UserInteractor getInstance(@NonNull IStorage storage, @NonNull Api api) {
        UserInteractor localInstance = instance;
        if (localInstance == null) {
            synchronized (UserInteractor.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new UserInteractor(storage, api);
                }
            }
        }
        return localInstance;
    }

    private static final String KEY_USER                    = "user";
    private static final String KEY_PHONE                   = "phone";
    private static final String KEY_PHONE_CODE_ID           = "phoneCodeId";
    private static final String KEY_FIRST_VISIT_AFTER_LOGIN = "firstVisitAfterLogin";


    @NonNull private final IStorage storage;
    @NonNull private final Api      api;

    public UserInteractor(@NonNull IStorage storage, @NonNull Api api) {
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
            .map(saveUser());
    }

    public Observable<Void> verifyCode(@NonNull String verificationCode) {
        return api.signIn(verificationCode, storage.getString(KEY_PHONE), storage.getString(KEY_PHONE_CODE_ID))
            .map(saveUser());
    }

    @NonNull private Func1<UserResponse, Void> saveUser() {
        return result -> {
            storage.remove(KEY_PHONE);
            storage.remove(KEY_PHONE_CODE_ID);
            saveUser(result.getUser());
            return null;
        };
    }


    @Nullable public String getToken() {
        User user = getUser();
        return user == null ? null : user.getAuthenticationToken();
    }


    public String getUserId() {
        return getUser().getId();
    }

    public boolean userHasToken() {
        User user = getUser();
        return user != null && user.getAuthenticationToken() != null;
    }

    public void logout() {
        storage.remove(KEY_USER);
    }

    public boolean firstVisitAfterLogin() {
        return storage.getBoolean(KEY_FIRST_VISIT_AFTER_LOGIN, true);
    }

    public void trackFirstVisit() {
        storage.putBoolean(KEY_FIRST_VISIT_AFTER_LOGIN, false);
    }

    public String getPhone() {
        return getUser().getPhoneNumber();
    }

    public void clearAll() {
        storage.clear();
    }

    public Observable<UserResponse> updateUser(@NonNull String firstName,
                                               @NonNull String lastName,
                                               @NonNull String email) {
        return api.editProfile(getUserId(), firstName, lastName, email)
            .map(result -> {
                saveUser(result.getUser());
                return result;
            });
    }

    public void saveUser(@NonNull User user) {
        storage.put(KEY_USER, user);
    }

    public User getUser() {
        return storage.get(KEY_USER, User.class);
    }
}
