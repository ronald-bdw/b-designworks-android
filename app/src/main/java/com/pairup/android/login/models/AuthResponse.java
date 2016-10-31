package com.pairup.android.login.models;

import android.support.annotation.VisibleForTesting;

/**
 * Created by Ilya Eremin on 12.08.2016.
 */
public class AuthResponse {

    public static class _AuthResponse {
        private String  id;
        private boolean phoneRegistered;
    }

    public AuthResponse() {}

    @VisibleForTesting
    public AuthResponse(boolean isPhoneRegistered, String phoneCodeId) {
        authPhoneCode = new _AuthResponse();
        authPhoneCode.id = phoneCodeId;
        authPhoneCode.phoneRegistered = isPhoneRegistered;
    }

    private _AuthResponse authPhoneCode;

    public boolean isPhoneRegistered() {
        return authPhoneCode.phoneRegistered;
    }

    public String getPhoneCodeId() {
        return authPhoneCode.id;
    }
}
