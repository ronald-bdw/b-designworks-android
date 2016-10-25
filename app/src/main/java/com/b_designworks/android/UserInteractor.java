package com.b_designworks.android;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.b_designworks.android.chat.UserProfileUpdatedEvent;
import com.b_designworks.android.login.models.AuthResponse;
import com.b_designworks.android.login.models.FitToken;
import com.b_designworks.android.login.models.Integration;
import com.b_designworks.android.login.models.User;
import com.b_designworks.android.login.models.UserResponse;
import com.b_designworks.android.sync.FitBitAuthorizationStateChangedEvent;
import com.b_designworks.android.sync.GoogleFitAuthorizationStateChangedEvent;
import com.b_designworks.android.sync.Provider;
import com.b_designworks.android.utils.Bus;
import com.b_designworks.android.utils.Logger;
import com.b_designworks.android.utils.Rxs;
import com.b_designworks.android.utils.storage.IStorage;
import com.b_designworks.android.utils.storage.UserSettings;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


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
        Bus.event(UserProfileUpdatedEvent.EVENT);
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

    public Observable<FitToken> integrateGoogleFit(@NonNull String serverAuthCode) {
        return api.integrateFitnessApp(serverAuthCode, Provider.GOOGLE_FIT);
    }

    public Observable<FitToken> integrateFitbit(@NonNull String serverAuthCode) {
        return api.integrateFitnessApp(serverAuthCode, Provider.FITBIT);
    }

    public Observable<User> updateUserProfile() {
        return api.currentUser().map(response -> {
            saveUser(response.getUser());
            return response.getUser();
        });
    }

    public String getFullName() {
        User user = getUser();
        return user.getFirstName() + " " + user.getLastName();
    }

    public boolean isNotificationsEnabled() {
        return storage.getBoolean(KEY_NOTIFICATIONS_ENABLED, true);
    }

    public void setNotificationsEnabled(boolean enabled) {
        storage.putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled);
        if (enabled) {
            api.userEnabledPushNotifications("message_push")
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {}, ignoreError -> {});
        } else {
            api.userDisabledPushNotificatinos().subscribeOn(Schedulers.io())
                .subscribe(result -> {}, ignoreError -> {});
        }
    }

    public void removeFitnessToken(@NonNull Provider provider) {
        String tokenId = getFitnessToken(provider);
        if (tokenId != null) {
            api.deleteFitnessToken(tokenId)
                .subscribeOn(Schedulers.io())
                .subscribe(result -> removeFitnessTokenLocally(result.getId()), Logger::e);
        }
    }

    private void removeFitnessTokenLocally(@NonNull String tokenId) {
        User user = getUser();
        for (Integration integration : user.getIntegrations()) {
            if (integration.getFitnessTokenId() != null) {
                if (tokenId.equals(integration.getFitnessTokenId())) {
                    integration.setFitnessTokenId(null);
                    integration.setStatus(false);
                    saveUser(user);
                    if (Provider.GOOGLE_FIT == integration.getName()) {
                        Bus.event(GoogleFitAuthorizationStateChangedEvent.EVENT);
                    } else if (Provider.FITBIT == integration.getName()) {
                        Bus.event(FitBitAuthorizationStateChangedEvent.EVENT);
                    }
                }
            }
        }
    }

    public void saveFitnessTokenLocally(@NonNull String tokenId, @NonNull Provider provider) {
        User user = getUser();
        for (Integration integration : user.getIntegrations()) {
            if (provider == integration.getName()) {
                integration.setStatus(true);
                integration.setFitnessTokenId(tokenId);
                saveUser(user);
                if (provider == Provider.GOOGLE_FIT) {
                    Bus.event(GoogleFitAuthorizationStateChangedEvent.EVENT);
                } else if (provider == Provider.FITBIT) {
                    Bus.event(FitBitAuthorizationStateChangedEvent.EVENT);
                }
            }
        }
    }

    public boolean isFitnessAuthEnabled(@NonNull Provider provider) {
        for (Integration integration : getUser().getIntegrations()) {
            if (provider == integration.getName() && integration.getStatus()) {
                return true;
            }
        }
        return false;
    }

    public @Nullable String getFitnessToken(@NonNull Provider provider) {
        String id = null;
        for (Integration integration : getUser().getIntegrations()) {
            if (provider == integration.getName()) {
                id = integration.getFitnessTokenId();
            }
        }
        return id;
    }
}
