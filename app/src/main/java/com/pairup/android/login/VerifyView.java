package com.pairup.android.login;

import android.support.annotation.NonNull;

/**
 * Created by Ilya Eremin on 9/21/16.
 */
public interface VerifyView {

    void showRequestVerificationCodeProgressDialog();

    void showError(Throwable error);

    void hideRequestVerificationProgressDialog();

    void showVerificationCodeError();

    void showAuthorizationProgressDialog();

    void hideAuthProgressDialog();

    void openChatScreen();

    void openTourScreen();

    void openRegistrationScreen(@NonNull String phone, @NonNull String code,
                                @NonNull String phoneCodeId);
}
