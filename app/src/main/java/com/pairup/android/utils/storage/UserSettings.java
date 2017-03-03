package com.pairup.android.utils.storage;

import android.support.annotation.NonNull;

/**
 * Created by Ilya Eremin on 9/21/16.
 */

public class UserSettings {

    private static final String KEY_TOKEN             = "token";
    private static final String KEY_PHONE             = "phone";
    private static final String KEY_FIRST_POPUP_SHOWN = "subscriptionExpiringSoonMessageShown";

    private final IStorage storage;

    public UserSettings(IStorage storage) {
        this.storage = storage;
    }

    public void saveAuthInfo(@NonNull String token, @NonNull String phone) {
        storage.putString(KEY_TOKEN, token);
        storage.putString(KEY_PHONE, phone);
    }

    public boolean userHasToken() {
        return storage.contains(KEY_TOKEN);
    }

    public String getToken() {
        return storage.getString(KEY_TOKEN);
    }

    public String getPhone() {
        return storage.getString(KEY_PHONE);
    }

    public void saveSubscriptionExpiringSoonMessageShown() {
        storage.putBoolean(KEY_FIRST_POPUP_SHOWN, true);
    }

    public boolean subscriptionExpiringSoonMessageShown() {
        return storage.getBoolean(KEY_FIRST_POPUP_SHOWN, true);
    }

    public void clear() {
        storage.remove(KEY_TOKEN);
        storage.remove(KEY_PHONE);
        storage.remove(KEY_FIRST_POPUP_SHOWN);
    }
}