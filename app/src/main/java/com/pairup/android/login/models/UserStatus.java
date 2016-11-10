package com.pairup.android.login.models;

/**
 * Created by Klymenko on 08.11.2016.
 */

public class UserStatus {

    private boolean phoneRegistered;
    private String provider;

    public boolean isPhoneRegistered() {
        return phoneRegistered;
    }

    public void setPhoneRegistered(boolean phoneRegistered) {
        this.phoneRegistered = phoneRegistered;
    }

    public boolean hasHbfProvider(){
        return "HBF".equals(provider);
    }
}
