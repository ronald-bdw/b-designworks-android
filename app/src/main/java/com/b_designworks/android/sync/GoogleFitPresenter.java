package com.b_designworks.android.sync;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import javax.inject.Inject;

import static com.b_designworks.android.sync.GoogleFitScreen.mClient;

/**
 * Created by Ilya Eremin on 9/13/16.
 */

public class GoogleFitPresenter {

    private static final String TAG = "GoogleFitPresenter";
    private static final int REQUEST_CODE_SIGN_IN = 5599;
    private static final String SERVER_KEY = "326598618018-m18iigq8qr141cbgc7sv933p3m982sum.apps.googleusercontent.com";

    private final GoogleFitView view;
    private final GoogleFitInteractor interactor;

    @Inject
    public GoogleFitPresenter(GoogleFitView view, GoogleFitInteractor interactor) {
        this.view = view;
        this.interactor = interactor;
    }

    public void startIntegrate(@NonNull Activity activity) {
        GoogleSignInOptions signInRequest = new GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(new Scope(Scopes.FITNESS_ACTIVITY_READ))
            .requestServerAuthCode(SERVER_KEY, false)
            .build();

        mClient = new GoogleApiClient.Builder(context)
            .addApi(Auth.GOOGLE_SIGN_IN_API, signInRequest)
            .addConnectionCallbacks(
                new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Log.i(TAG, "Connected!!!");
                        uiStartIntegration.setEnabled(true);
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                            Log.i(TAG, "Connection lost.  Cause: Network Lost.");
                        } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                            Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
                        }
                    }
                }
            )
            .enableAutoManage(this, 0, result -> {
                Toast.makeText(GoogleFitScreen.this, "Exception while connecting to Google Play services: " +
                    result.getErrorMessage(), Toast.LENGTH_LONG).show();
                Log.i(TAG, "Google Play services connection failed. Cause: " +
                    result.toString());
            })
            .build();
        interactor.requestGoogleCode(activity);
    }

    public void handleResponse(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                if (acct != null) {
                    interactor.sendGoogleCodeToServer(acct.getServerAuthCode());
                    view.codeRetrievedSuccessfull();
                } else {
                    view.errorWhileRetrievingCode();
                }
            }
        }
    }
}
