package com.pairup.android.login.models;

import android.support.annotation.NonNull;

/**
 * Created by Klymenko on 08.11.2016.
 */

public class UserStatus {

    private boolean phoneRegistered;
    private String provider;

    public boolean isPhoneRegistered() {
        return phoneRegistered;
    }

    public boolean userHasProvider() {
        return provider != null ? !provider.isEmpty() : false;
    }

    public boolean isCorrectProvider(@NonNull String providerName) {
        return providerName.equals(provider);
    }
}
