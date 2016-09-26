package com.b_designworks.android.sync;

/**
 * Created by Ilya Eremin on 9/26/16.
 */
public interface FitbitView {
    void showSendingFitbitCodeProgress();
    void dismissSendingFitbitCodeProgress();
    void fitbitSuccessfullyIntegrated();
    void showError(Throwable error);
}
