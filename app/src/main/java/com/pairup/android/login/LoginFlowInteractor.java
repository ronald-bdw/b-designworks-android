package com.pairup.android.login;

import android.support.annotation.NonNull;

import com.pairup.android.utils.storage.IStorage;

/**
 * Created by Ilya Eremin on 10/25/16.
 */
public class LoginFlowInteractor {

    private static final String PREFIX = "loginFlow";

    private static final String KEY_PHONE_CODE_ID            = PREFIX + "phoneCodeId";
    private static final String KEY_PHONE_REGISTERED         = PREFIX + "phoneRegistered";
    private static final String KEY_PHONE_NUMBER             = PREFIX + "phoneNumber";
    private static final String KEY_USER_FILLED_REGISTRATION = PREFIX + "userFilledRegistration";
    private static final String KEY_FIRST_NAME               = PREFIX + "firstName";
    private static final String KEY_LAST_NAME                = PREFIX + "lastName";
    private static final String KEY_EMAIL                    = PREFIX + "email";
    private static final String KEY_HAS_PROVIDER = PREFIX + "hasProvider";

    private final IStorage storage;

    public LoginFlowInteractor(IStorage storage) {
        this.storage = storage;
    }

    public void setPhoneCodeId(String phoneCodeId) {
        storage.putString(KEY_PHONE_CODE_ID, phoneCodeId);
    }

    public void setPhoneRegistered(boolean phoneRegistered) {
        storage.putBoolean(KEY_PHONE_REGISTERED, phoneRegistered);
    }

    public String getPhoneNumber() {
        return storage.getString(KEY_PHONE_NUMBER);
    }

    public boolean isUserRegistered() {
        return storage.getBoolean(KEY_PHONE_REGISTERED, false);
    }

    public String getPhoneCodeId() {
        return storage.getString(KEY_PHONE_CODE_ID);
    }

    public void saveRegistrationData(@NonNull String firstName,
                                     @NonNull String lastName,
                                     @NonNull String email) {
        storage.putBoolean(KEY_USER_FILLED_REGISTRATION, true);
        storage.putString(KEY_FIRST_NAME, firstName);
        storage.putString(KEY_LAST_NAME, lastName);
        storage.putString(KEY_EMAIL, email);
    }

    public String getFirstName() {
        return storage.getString(KEY_FIRST_NAME);
    }

    public String getLastName() {
        return storage.getString(KEY_LAST_NAME);
    }

    public String getEmail() {
        return storage.getString(KEY_EMAIL);
    }

    public void setPhoneNumber(String phoneNumber) {
        storage.putString(KEY_PHONE_NUMBER, phoneNumber);
    }

    public void reset() {
        storage.remove(KEY_PHONE_CODE_ID);
        storage.remove(KEY_PHONE_REGISTERED);
        storage.remove(KEY_PHONE_NUMBER);
        storage.remove(KEY_USER_FILLED_REGISTRATION);
        storage.remove(KEY_FIRST_NAME);
        storage.remove(KEY_LAST_NAME);
        storage.remove(KEY_EMAIL);
    }

    public boolean userAlreadyFilledRegistration() {
        return storage.getBoolean(KEY_USER_FILLED_REGISTRATION, false);
    }

    public boolean userEnteredPhone() {
        return storage.contains(KEY_PHONE_NUMBER);
    }

    public void setHasProvider(boolean hasProvider) {
        storage.putBoolean(KEY_HAS_PROVIDER, hasProvider);
    }

    public boolean hasProvider() {
        return storage.getBoolean(KEY_HAS_PROVIDER, false);
    }
}