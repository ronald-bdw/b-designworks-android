package com.pairup.android.login.models;

import java.util.Locale;

/**
 * Created by Klymenko on 08.11.2016.
 */

public class UserStatus {

    private boolean phoneRegistered;
    private String  provider;

    public void setPhoneRegistered(boolean phoneRegistered) {
        this.phoneRegistered = phoneRegistered;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public boolean isPhoneRegistered() {
        return phoneRegistered;
    }

    public String getProvider() {
        return provider;
    }

    public boolean userHasProvider() {
        return provider != null && !provider.isEmpty() &&
            !provider.toLowerCase(Locale.ENGLISH).equals("subscriber");
    }
}
