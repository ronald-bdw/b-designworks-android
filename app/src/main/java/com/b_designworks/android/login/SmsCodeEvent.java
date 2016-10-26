package com.b_designworks.android.login;

/**
 * Created by Ilya Eremin on 18.08.2016.
 */
public class SmsCodeEvent {

    private final String verificationCode;

    public SmsCodeEvent(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getCode() {
        return verificationCode;
    }
}
