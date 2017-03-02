package com.pairup.android;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;

import com.pairup.android.chat.UserProfileUpdatedEvent;
import com.pairup.android.login.models.AuthResponse;
import com.pairup.android.login.models.FitToken;
import com.pairup.android.login.models.Integration;
import com.pairup.android.login.models.Providers;
import com.pairup.android.login.models.User;
import com.pairup.android.login.models.UserResponse;
import com.pairup.android.login.models.UserStatus;
import com.pairup.android.sync.FitBitAuthorizationStateChangedEvent;
import com.pairup.android.sync.GoogleFitAuthorizationStateChangedEvent;
import com.pairup.android.sync.Provider;
import com.pairup.android.utils.Bus;
import com.pairup.android.utils.Logger;
import com.pairup.android.utils.storage.IStorage;
import com.pairup.android.utils.storage.UserSettings;

import java.io.File;

import io.smooch.core.Smooch;
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
    private static final String KEY_SHOW_TOUR_TO_USER       = "showTourForUser";
    private static final String KEY_FIRST_VISIT_AFTER_LOGIN = "firstVisitAfterLogin";
    private static final String DEVICE_TYPE_ANDROID         = "android";

    @NonNull private final IStorage                  storage;
    @NonNull private final UserSettings              userSettings;
    @NonNull private final Api                       api;
    @NonNull private final NotificationManagerCompat notificationManager;

    public UserInteractor(@NonNull IStorage storage,
                          @NonNull UserSettings userSettings,
                          @NonNull Api api,
                          @NonNull NotificationManagerCompat notificationManager) {
        this.storage = storage;
        this.userSettings = userSettings;
        this.api = api;
        this.notificationManager = notificationManager;
    }

    public Observable<AuthResponse> requestCode(@NonNull String phone) {
        return api.sendMeCode(phone, DEVICE_TYPE_ANDROID);
    }

    public Observable<UserStatus> requestUserStatus(@NonNull String phone) {
        return api.getUserStatus(phone);
    }

    public Observable<Void> register(@NonNull String firstName, @NonNull String lastName,
                                     @NonNull String email, @NonNull String code,
                                     @NonNull String phone, @NonNull String phoneCodeId) {
        return api
            .register(firstName, lastName, email, code, phone, phoneCodeId, DEVICE_TYPE_ANDROID)
            .map(saveUser());
    }

    public Observable<Object> login(@NonNull String verificationCode,
                                    @NonNull String phone,
                                    @NonNull String phoneCodeId) {
        return api.signIn(verificationCode, phone, phoneCodeId, DEVICE_TYPE_ANDROID)
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

    public String getUserZendeskId() {
        return getUser().getZendeskId();
    }

    public void logout() {
        storage.remove(KEY_USER);
        userSettings.clear();
        Smooch.logout();
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
        return api.uploadAvatar(getUserId(), MultipartBody.Part.createFormData("user[avatar]",
            imageUrl, body)).map(result -> {
            saveUser(result.getUser());
            return result;
        });
    }

    public Observable<FitToken> integrateFitnessApp(@NonNull String serverAuthCode,
                                                    @NonNull Provider provider) {
        return api.integrateFitnessApp(serverAuthCode, provider);
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

    public boolean areNotificationsEnabled() {
        if (DeviceInteractor.doesSdkSupportNotifications()) {
            return notificationManager.areNotificationsEnabled();
        } else {
            return true;
        }
    }

    public void sendNotificationsStatus() {
        if (areNotificationsEnabled()) {
            api.userEnabledPushNotifications("message_push")
                .subscribeOn(Schedulers.io())
                .subscribe(result -> { }, ignoreError -> { });
        } else {
            api.userDisabledPushNotificatinos().subscribeOn(Schedulers.io())
                .subscribe(result -> { }, ignoreError -> { });
        }
    }

    public void removeFitnessToken(@NonNull Provider provider) {
        String tokenId = getFitnessToken(provider);
        if (tokenId != null) {
            api.deleteFitnessToken(tokenId)
                .subscribeOn(Schedulers.io())
                .subscribe(result -> removeFitnessTokenLocally(provider), Logger::e);
        }
    }

    private void removeFitnessTokenLocally(@NonNull Provider provider) {
        User user = getUser();
        for (Integration integration : user.getIntegrations()) {
            if (provider == integration.getProvider()) {
                integration.setFitnessTokenId(null);
                integration.setStatus(false);
                saveUser(user);
                if (Provider.GOOGLE_FIT == integration.getProvider()) {
                    Bus.event(GoogleFitAuthorizationStateChangedEvent.EVENT);
                } else if (Provider.FITBIT == integration.getProvider()) {
                    Bus.event(FitBitAuthorizationStateChangedEvent.EVENT);
                }
            }
        }
    }

    public void saveFitnessTokenLocally(@NonNull String tokenId, @NonNull Provider provider) {
        User user = getUser();
        for (Integration integration : user.getIntegrations()) {
            if (provider == integration.getProvider()) {
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
            if (provider == integration.getProvider() && integration.getStatus()) {
                return true;
            }
        }
        return false;
    }

    @Nullable public String getFitnessToken(@NonNull Provider provider) {
        String id = null;
        for (Integration integration : getUser().getIntegrations()) {
            if (provider == integration.getProvider()) {
                id = integration.getFitnessTokenId();
            }
        }
        return id;
    }

    public boolean userLoggedIn() {
        return storage.contains(KEY_USER);
    }

    public void setShowTourForUser() {
        storage.putBoolean(KEY_SHOW_TOUR_TO_USER, false);
    }

    public boolean showTourForUser() {
        return storage.getBoolean(KEY_SHOW_TOUR_TO_USER, true);
    }

    public Observable<Void> sendInAppStatus(@NonNull String planName, @NonNull String purchasedDate,
                                            @NonNull String expiresDate, boolean isActive) {
        return api.sendSubscriptionStatus(planName, purchasedDate, expiresDate, isActive)
            .map(result -> null);
    }

    public Observable<Void> sendInAppStatusExpired() {
        return api.sendSubscriptionExpired();
    }

    public Observable<Void> checkVerificationNumber(@NonNull String id, @NonNull String code) {
        return api.checkVerificationCode(id, code).map(result -> null);
    }

    public Observable<Providers> getProviders() {
        return api.getProviders();
    }

    public void sendTimeZoneToServer(@NonNull String timeZone) {
        api.sendTimeZone(getUserId(), timeZone)
            .subscribeOn(Schedulers.io())
            .subscribe(result -> { }, ignoreError -> { });
    }

    public void savefirstPopupShown(boolean firstPopupShown) {
        userSettings.saveFirstPopupShown(firstPopupShown);
    }

    public boolean firstPopupShown() {
        return userSettings.firstPopupShown();
    }

}
