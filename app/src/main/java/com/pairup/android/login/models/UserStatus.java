package com.pairup.android.login.models;

/**
 * Created by Klymenko on 08.11.2016.
 */

public class UserStatus {

    private boolean phoneRegistered;
    private String  provider;

    public boolean isPhoneRegistered() {
        return phoneRegistered;
    }

    public boolean userHasHbfProvider() {
        return "HBF".equals(provider);
    }
}
