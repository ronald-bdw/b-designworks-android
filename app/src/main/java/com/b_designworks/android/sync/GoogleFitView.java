package com.b_designworks.android.sync;

import com.google.android.gms.common.ConnectionResult;

/**
 * Created by Ilya Eremin on 9/19/16.
 */
public interface GoogleFitView {
    void codeRetrievedSuccessfull();
    void errorWhileRetrievingCode();
    void onGoogleServicesError(ConnectionResult result);
    void showInternetConnectionError();
    void showGoogleServiceDisconected();
    void onError(Throwable error);
    void userCancelIntegration();
}
