package com.pairup.android.login.models;

/**
 * Created by Klymenko on 08.11.2016.
 */

public class UserExistResponse {

    private boolean phoneRegistered;

    public boolean isPhoneRegistered() {
        return phoneRegistered;
    }

    public void setPhoneRegistered(boolean phoneRegistered) {
        this.phoneRegistered = phoneRegistered;
    }
}
