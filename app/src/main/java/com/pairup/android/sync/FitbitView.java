package com.pairup.android.sync;

/**
 * Created by Ilya Eremin on 9/26/16.
 */
public interface FitbitView {
    void showSendingFitbitCodeProgress();

    void dismissSendingFitbitCodeProgress();

    void fitbitSuccessfullyIntegrated();

    void onError(Throwable error);
}
