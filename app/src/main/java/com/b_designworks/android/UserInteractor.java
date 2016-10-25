package com.b_designworks.android;

import android.support.annotation.NonNull;

import com.b_designworks.android.chat.UserProfileUpdatedEvent;
import com.b_designworks.android.login.models.AuthResponse;
import com.b_designworks.android.login.models.User;
import com.b_designworks.android.login.models.UserResponse;
import com.b_designworks.android.sync.Provider;
import com.b_designworks.android.utils.Bus;
import com.b_designworks.android.utils.storage.IStorage;
import com.b_designworks.android.utils.storage.UserSettings;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;


/**
 * Created by Ilya Eremin on 15.08.2016.
 */
public class UserInteractor {

    private static final String KEY_USER                    = "user";
    private static final String KEY_FIRST_VISIT_AFTER_LOGIN = "firstVisitAfterLogin";
    private static final String KEY_NOTIFICATIONS_ENABLED   = "notificationsEnabled";

    @NonNull private final IStorage     storage;
    @NonNull private final UserSettings userSettings;
    @NonNull private final Api          api;

    public UserInteractor(@NonNull IStorage storage,
                          @NonNull UserSettings userSettings,
                          @NonNull Api api) {
        this.storage = storage;
        this.userSettings = userSettings;
        this.api = api;
    }

    public Observable<AuthResponse> requestCode(@NonNull String phone) {
        return api.sendMeCode(phone);
    }

    public Observable<Void> register(@NonNull String firstName, @NonNull String lastName,
                                     @NonNull String email, @NonNull String code,
                                     @NonNull String phone, @NonNull String phoneCodeId) {
        return api.register(firstName, lastName, email, code, phone, phoneCodeId)
            .map(saveUser());
    }

    public Observable<Object> verifyCode(
        @NonNull String verificationCode, @NonNull String phone, @NonNull String phoneCodeId) {
        return api.signIn(verificationCode, phone, phoneCodeId)
            .map(saveUser());
    }

    @NonNull private Func1<UserResponse, Void> saveUser() {
        return result -> {
            saveUser(result.getUser());
            return null;
        };
    }

    public String getUserId() {
        return getUser().getId();
    }

    public void logout() {
        storage.remove(KEY_USER);
        userSettings.clear();
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
        userSettings.saveAuthInfo(user.getAuthenticationToken(), user.getPhoneNumber());
    }

    public User getUser() {
        return storage.get(KEY_USER, User.class);
    }

    public Observable<UserResponse> uploadAvatar(String imageUrl) {
        RequestBody body = RequestBody.create(MediaType.parse("image/jpg"), new File(imageUrl));
        return api.uploadAvatar(getUserId(), MultipartBody.Part.createFormData("user[avatar]", imageUrl, body)).map(result -> {
            saveUser(result.getUser());
            return result;
        });
    }


    public Observable<Object> integrateFitbit(@NonNull String code) {
        return api.integrateFitnessApp(code, Provider.FITBIT)
            .map(result -> null);
    }

    public Observable<User> updateUserProfile() {
        return api.currentUser().map(response -> {
            saveUser(response.getUser());
            Bus.event(UserProfileUpdatedEvent.EVENT);
            return response.getUser();
        });
    }

    public String getFullName() {
        User user = getUser();
        return user.getFirstName() + " " + user.getLastName();
    }

    public boolean isNotificationsEnabled() {
        return storage.getBoolean(KEY_NOTIFICATIONS_ENABLED, false);
    }

    public void setNotificationsEnabled(boolean enabled) {
        storage.putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled);
    }

    public boolean userHasValidSubscription() {
        return false;
    }
}
