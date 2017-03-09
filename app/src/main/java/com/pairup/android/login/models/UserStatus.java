package com.pairup.android.login.models;

import com.pairup.android.utils.Strings;

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
        return !Strings.isEmpty(provider) && !provider.equalsIgnoreCase("subscriber");
    }
}
