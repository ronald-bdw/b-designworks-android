package com.pairup.android.sync;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.pairup.android.UserInteractor;
import com.pairup.android.utils.Rxs;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import javax.inject.Inject;

/**
 * Created by Ilya Eremin on 9/13/16.
 */

public class GoogleFitPresenter {

    private static final String TAG                  = "GoogleFitPresenter";
    private static final int    REQUEST_CODE_SIGN_IN = 5599;
    private static final String SERVER_KEY           =
        "1031731430214-cgrtpcni90gh3lmmr3fem5nihb4b3iuk.apps.googleusercontent.com";

    @Nullable private GoogleFitView view;

    private final UserInteractor  userInteractor;
    private final Context         context;
    private       GoogleApiClient mClient;

    @Inject
    public GoogleFitPresenter(@NonNull UserInteractor userInteractor,
                              @NonNull Context context) {
        this.userInteractor = userInteractor;
        this.context = context;
    }

    public void attachView(GoogleFitView view, @NonNull FragmentActivity activity) {
        this.view = view;
        buildGoogleFitClient(activity);
    }

    private void buildGoogleFitClient(@NonNull FragmentActivity activity) {
        GoogleSignInOptions signInRequest = new GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(new Scope(Scopes.FITNESS_ACTIVITY_READ))
            .requestServerAuthCode(SERVER_KEY, true)
            .build();
        mClient = new GoogleApiClient.Builder(context)
            .addApi(Auth.GOOGLE_SIGN_IN_API, signInRequest)
            .addConnectionCallbacks(
                new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        if (view != null) {
                            view.onClientConnected();
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                            if (view != null) {
                                view.showInternetConnectionError();
                            }
                        } else if (i == GoogleApiClient
                            .ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                            if (view != null) {
                                view.showGoogleServiceDisconnected();
                            }
                        }
                    }
                }
            )
            .enableAutoManage(activity, 0, result -> {
                Log.i(TAG, "Google Play services connection failed. Cause: " + result.toString());
                if (view != null) {
                    view.onGoogleServicesError(result);
                }
            })
            .build();
    }

    public void startIntegrate(@NonNull FragmentActivity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mClient);
        activity.startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
    }

    public void handleResponse(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GoogleSignInAccount acct = result.getSignInAccount();
                    if (acct != null) {
                        userInteractor
                            .integrateFitnessApp(acct.getServerAuthCode(), Provider.GOOGLE_FIT)
                            .compose(Rxs.doInBackgroundDeliverToUI())
                            .subscribe(
                                fitToken -> userInteractor
                                    .saveFitnessTokenLocally(fitToken.getId(), Provider.GOOGLE_FIT),
                                (error) -> {
                                    if (view != null) {
                                        view.onError(error);
                                    }
                                });
                        if (view != null) {
                            view.integrationSuccessful();
                        }
                    } else {
                        if (view != null) {
                            view.errorWhileRetrievingCode();
                        }
                    }
                }
            } else {
                if (view != null) {
                    view.userCancelIntegration();
                }
            }
        }
    }

    public void onShown() {
    }

    public void detachView(@NonNull FragmentActivity activity) {
        if (mClient != null) {
            mClient.stopAutoManage(activity);
            mClient.disconnect();
            mClient = null;
        }
        view = null;
    }

    public void logout() {
        if (mClient != null) {
            Auth.GoogleSignInApi.signOut(mClient);
        }
    }
}
