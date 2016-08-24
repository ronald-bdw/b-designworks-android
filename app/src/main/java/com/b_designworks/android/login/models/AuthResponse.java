package com.b_designworks.android.login.models;

/**
 * Created by Ilya Eremin on 12.08.2016.
 */
public class AuthResponse {

    public static class _AuthResponse {
        private String  id;
        private boolean phoneRegistered;
    }

    private _AuthResponse authPhoneCode;

    public boolean isPhoneRegistered() {
        return authPhoneCode.phoneRegistered;
    }

    public String getPhoneCodeId() {
        return authPhoneCode.id;
    }
}
