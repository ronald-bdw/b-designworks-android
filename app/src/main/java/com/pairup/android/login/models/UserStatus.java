package com.pairup.android.login.models;

/**
 * Created by Klymenko on 08.11.2016.
 */

public class UserStatus {

    private boolean      phoneRegistered;
    private ProviderType provider;

    public boolean isPhoneRegistered() {
        return phoneRegistered;
    }

    public boolean userHasProvider() {
        return ProviderType.HBF == provider || ProviderType.BDW == provider;
    }

    public ProviderType getProvider() {
        return provider;
    }
}
