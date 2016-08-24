package com.b_designworks.android.login;

/**
 * Created by Ilya Eremin on 18.08.2016.
 */
public class SmsCodeEvent {

    private final String codeSmsText;

    public SmsCodeEvent(String codeSmsText) {
        this.codeSmsText = codeSmsText;
    }

    public String getCodeSmsText() {
        return codeSmsText;
    }

    public String getCode() {
        return "1234";
    }
}
