package com.pairup.android.login;

/**
 * Created by sergeyklymenko on 1/10/17.
 */

public interface EnterPhoneView {

    void showProgress();

    void hideKeyboard();

    void showErrorDialog();

    void hideProgress();

    void openVerificationScreen();

    void showDialogNetworkProblem();

    void showPhoneError();

    void handleError();

    void registrationErrorFillAreaCode();

    void registrationErrorFillPhone();

    void logWrongProvider();
}