package com.b_designworks.android.sync;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.login.models.Integration;
import com.b_designworks.android.login.models.User;
import com.b_designworks.android.utils.Bus;
import com.b_designworks.android.utils.Logger;
import com.b_designworks.android.utils.Rxs;
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
    private static final String SERVER_KEY           = "1031731430214-cgrtpcni90gh3lmmr3fem5nihb4b3iuk.apps.googleusercontent.com";

    @Nullable private GoogleFitView view;

    private final UserInteractor      userInteractor;
    private final GoogleFitInteractor googleFitInteractor;
    private final Context             context;
    private       GoogleApiClient     mClient;

    @Inject
    public GoogleFitPresenter(@NonNull UserInteractor userInteractor,
                              @NonNull GoogleFitInteractor googleFitInteractor,
                              @NonNull Context context) {
        this.userInteractor = userInteractor;
        this.googleFitInteractor = googleFitInteractor;
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
            .requestServerAuthCode(SERVER_KEY, false)
            .build();
        mClient = new GoogleApiClient.Builder(context)
            .addApi(Auth.GOOGLE_SIGN_IN_API, signInRequest)
            .addConnectionCallbacks(
                new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        view.enableIntegrationContainer(true);
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                            if (view != null) {
                                view.showInternetConnectionError();
                            }
                        } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                            if (view != null) {
                                view.showGoogleServiceDisconected();
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
        if (requestCode == REQUEST_CODE_SIGN_IN & resultCode == Activity.RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                if (acct != null) {
                    userInteractor.integrateGoogleFit(acct.getServerAuthCode())
                        .compose(Rxs.doInBackgroundDeliverToUI())
                        .subscribe(
                            fitToken -> userInteractor.saveGoogleFitTokenLocally(fitToken.getId()),
                            view::onError);
                    if (view != null) {
                        view.codeRetrievedSuccessfull();
                    }
                } else {
                    if (view != null) {
                        view.errorWhileRetrievingCode();
                    }
                }
            }
        }
    }

    public void onShown() {
    }

    public void detachView() {
        view = null;
        mClient = null;
    }

    public void logout() {
        String tokenId = getGoogleFitTokenId();
        if (mClient != null && tokenId!=null) {
            userInteractor.removeFitnessToken(tokenId);
            Auth.GoogleSignInApi.signOut(mClient);
        }
    }

    private @Nullable String getGoogleFitTokenId() {
        String id = null;
        for (Integration integration : userInteractor.getUser().getIntegrations()) {
            if (integration.getName().equals("Googlefit")) {
                id = integration.getFitnessTokenId();
            }
        }
        return id;
    }
}
