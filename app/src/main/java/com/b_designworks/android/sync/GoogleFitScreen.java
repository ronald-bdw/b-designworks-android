package com.b_designworks.android.sync;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.ui.UiInfo;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import butterknife.Bind;

/**
 * Created by Ilya Eremin on 24.08.2016.
 */
public class GoogleFitScreen extends BaseActivity {

    private static final int REQUEST_CODE_SIGN_IN = 5599;

    private static final String TAG = "GoogleFitScreen";

    private static final String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";
    private static final String SERVER_KEY  = "326598618018-m18iigq8qr141cbgc7sv933p3m982sum.apps.googleusercontent.com";

    public static GoogleApiClient mClient     = null;
    private       boolean         firstLaunch = true;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_google_fit);
    }

    @Bind(R.id.start_integration) View uiStartIntegration;

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        buildFitnessClient();
    }

    private void buildFitnessClient() {
        GoogleSignInOptions signInRequest = new GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(new Scope(Scopes.FITNESS_ACTIVITY_READ))
            .requestIdToken(SERVER_KEY)
            .build();
        mClient = new GoogleApiClient.Builder(this)
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
                        // If your connection to the sensor gets lost at some point,
                        // you'll be able to determine the reason and react to it here.
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
        if (firstLaunch) {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mClient);
            startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
            firstLaunch = false;
        }
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                String token = acct.getIdToken();
                Toast.makeText(this, token, Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override protected void onResume() {
        super.onResume();
        uiStartIntegration.setEnabled(mClient.isConnected());
    }

}
