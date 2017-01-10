package com.pairup.android.login.models;

import android.support.annotation.VisibleForTesting;

/**
 * Created by Ilya Eremin on 12.08.2016.
 */
public class AuthResponse {

    private InnerAuthResponse authPhoneCode;

    public AuthResponse() {
    }

    @VisibleForTesting
    public AuthResponse(boolean isPhoneRegistered, String phoneCodeId) {
        authPhoneCode = new InnerAuthResponse();
        authPhoneCode.id = phoneCodeId;
        authPhoneCode.phoneRegistered = isPhoneRegistered;
    }

    public void setAuthPhoneCode(InnerAuthResponse authPhoneCode) {
        this.authPhoneCode = authPhoneCode;
    }

    public boolean isPhoneRegistered() {
        return authPhoneCode.phoneRegistered;
    }

    public String getPhoneCodeId() {
        return authPhoneCode.id;
    }

    public static class InnerAuthResponse {
        private String  id;
        private boolean phoneRegistered;

        public void setId(String id) {
            this.id = id;
        }

        public void setPhoneRegistered(boolean phoneRegistered) {
            this.phoneRegistered = phoneRegistered;
        }
    }
}
