package com.pairup.android.sync;

import com.google.android.gms.common.ConnectionResult;

/**
 * Created by Ilya Eremin on 9/19/16.
 */
public interface GoogleFitView {
    void integrationSuccessful();

    void errorWhileRetrievingCode();

    void onGoogleServicesError(ConnectionResult result);

    void showInternetConnectionError();

    void showGoogleServiceDisconnected();

    void onError(Throwable error);

    void userCancelIntegration();

    void onClientConnected();
}
