package com.b_designworks.android;

import android.support.annotation.NonNull;

import com.b_designworks.android.login.models.AuthResponse;
import com.b_designworks.android.login.models.Integration;
import com.b_designworks.android.login.models.User;
import com.b_designworks.android.login.models.UserResponse;
import com.b_designworks.android.sync.FitBitAuthorizationEnabled;
import com.b_designworks.android.sync.GoogleFitAuthorizationEnabledEvent;
import com.b_designworks.android.sync.Provider;
import com.b_designworks.android.utils.Bus;
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

    public void integrateGoogleFit(@NonNull String serverAuthCode) {
        api.integrateFitnessApp(serverAuthCode, Provider.GOOGLE_FIT)
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(fitToken -> {
                saveGoogleFitTokenLocally(fitToken.getId());
            }, e -> {});
    }

    public void integrateFitbit(@NonNull String serverAuthCode) {
        api.integrateFitnessApp(serverAuthCode, Provider.FITBIT)
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(fitToken -> {
                saveFitBitTokenLocally(fitToken.getId());
            }, e -> {});
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
        return storage.getBoolean(KEY_NOTIFICATIONS_ENABLED, false);
    }

    public void setNotificationsEnabled(boolean enabled) {
        storage.putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled);
    }

    public void removeFitnessToken(@NonNull String id) {
        api.deleteFitnessToken(id)
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(result ->
            {
                removeFitnessTokenLocally(result.getId());
            }
            , error -> {});
    }

    private void removeFitnessTokenLocally(String tokenId) {
        User user = getUser();
        Integration[] integrations = user.getIntegrations();
        for (int i = 0; i < integrations.length; i++) {
            if (integrations[i].getFitnessTokenId().equals(tokenId)) {
                integrations[i].setStatus("false");
                integrations[i].setFitnessTokenId(null);
            }
        }
        user.setIntegrations(integrations);
        saveUser(user);
        //TODO
        Bus.event(GoogleFitAuthorizationEnabledEvent.EVENT);
        Bus.event(FitBitAuthorizationEnabled.EVENT);
    }

    private void saveGoogleFitTokenLocally(@NonNull String tokenId) {
        User user = getUser();
        Integration[] integrations = user.getIntegrations();
        for (int i = 0; i < integrations.length; i++) {
            if (integrations[i].getName().equals("Googlefit")) {
                integrations[i].setStatus("true");
                integrations[i].setFitnessTokenId(tokenId);
            }
        }
        user.setIntegrations(integrations);
        saveUser(user);
        Bus.event(GoogleFitAuthorizationEnabledEvent.EVENT);
    }

    private void saveFitBitTokenLocally(@NonNull String tokenId) {
        User user = getUser();
        Integration[] integrations = user.getIntegrations();
        for (int i = 0; i < integrations.length; i++) {
            if (integrations[i].getName().equals("Fitbit")) {
                integrations[i].setStatus("true");
                integrations[i].setFitnessTokenId(tokenId);
            }
        }
        user.setIntegrations(integrations);
        saveUser(user);
        Bus.event(FitBitAuthorizationEnabled.EVENT);
    }

    public boolean isGoogleFitAuthEnabled() {
        for (Integration integration : getUser().getIntegrations()) {
            if (integration.getName().equals("Googlefit") && integration.getStatus().equals("true")) {
                return true;
            }
        }
        return false;
    }

    public boolean isFitBitAuthEnabled() {
        for (Integration integration : getUser().getIntegrations()) {
            if (integration.getName().equals("Fitbit") && integration.getStatus().equals("true")) {
                return true;
            }
        }
        return false;
    }
}
