package com.b_designworks.android.sync;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Toast;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.BuildConfig;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.utils.Bus;
import com.b_designworks.android.utils.di.Injector;
import com.b_designworks.android.utils.network.ErrorUtils;
import com.b_designworks.android.utils.ui.SimpleDialog;
import com.b_designworks.android.utils.ui.SimpleLoadingDialog;
import com.b_designworks.android.utils.ui.UiInfo;
import com.google.android.gms.common.ConnectionResult;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class SyncScreen extends BaseActivity implements GoogleFitView, FitbitView {

    private static final String GETTING_CODE_URL = "https://www.fitbit.com/oauth2/" +
        "authorize?response_type=code" +
        "&client_id=" + BuildConfig.FITBIT_APP_ID +
//        "&redirect_uri=pearup%3A%2F%2Fpearup.com"+
        "&scope=activity";

    private static final String KEY_FITBIT_CODE = "fitbitCode";

    @Bind(R.id.sync_container)           View         uiSyncContainer;
    @Bind(R.id.google_fit_switch_compat) SwitchCompat uiGoogleFitSC;
    @Bind(R.id.fitbit_switch_compat)     SwitchCompat uiFitBitSC;

    @Inject UserInteractor     userInteractor;
    @Inject GoogleFitPresenter googleFitPresenter;

    @Nullable         String          code;
    @Nullable         Subscription    sendingFitbitCodeSubs;
    @Nullable private ProgressDialog  progressDialog;
    private           FitbitPresenter fitbitPresenter;

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
        Bus.subscribe(this);
        googleFitPresenter.attachView(this, this);
        uiGoogleFitSC.setChecked(userInteractor.isGoogleFitAuthEnabled());
        uiFitBitSC.setChecked(userInteractor.isFitBitAuthEnabled());

        fitbitPresenter = new FitbitPresenter(this, userInteractor);
        Intent intent = getIntent();
        handleIntent(intent);
    }

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_sync).enableBackButton().setTitleRes(R.string.title_sync);
    }

    @OnClick(R.id.google_fit) void onGoogleFitClick() {
        if (userInteractor.isGoogleFitAuthEnabled()) {
            googleFitPresenter.logout();
        } else {
            googleFitPresenter.startIntegrate(this);
        }
    }

    @OnClick(R.id.fitbit) void onFitbitClick() {

        if(userInteractor.isFitBitAuthEnabled()){
            //TODO
            userInteractor.removeFitnessToken(userInteractor.getUser().getIntegrations()[1].getFitnessTokenId());
        } else {
            Navigator.openUrl(context(), GETTING_CODE_URL);
        }
    }

    //Google Fit
    @Override public void codeRetrievedSuccessfull() {
        Toast.makeText(this, R.string.google_fit_token_retrieved, Toast.LENGTH_SHORT).show();
    }

    @Override public void errorWhileRetrievingCode() {
        Toast.makeText(this, R.string.google_fit_fail_to_retrieve_token, Toast.LENGTH_SHORT).show();
    }

    @Override public void onGoogleServicesError(ConnectionResult result) {
        Toast.makeText(SyncScreen.this, "Exception while connecting to Google Play services: " +
            result.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    @Override public void enableIntegrationContainer(boolean enabled) {
        uiSyncContainer.setEnabled(enabled);
    }

    @Override public void showInternetConnectionError() {
        SimpleDialog.networkProblem(context());
    }

    @Override public void showGoogleServiceDisconected() {

    }

    @Override public void onError(Throwable error) {
        ErrorUtils.handle(context(), error);
    }

    //FitBit
    @Override public void showSendingFitbitCodeProgress() {
        progressDialog = SimpleLoadingDialog.show(context(), getString(R.string.sending_fitbit_code), () -> {
            if (sendingFitbitCodeSubs != null) {
                sendingFitbitCodeSubs.unsubscribe();
                sendingFitbitCodeSubs = null;
            }
        });
    }

    @Override public void dismissSendingFitbitCodeProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override public void fitbitSuccessfullyIntegrated() {
        Toast.makeText(context(), R.string.fitbit_integration_successful, Toast.LENGTH_SHORT).show();
    }

    private void handleIntent(Intent intent) {
        if (intent != null && intent.getData() != null) {
            code = intent.getData().getQueryParameter("code");
            fitbitPresenter.handleCode(code);
        }
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleFitPresenter.handleResponse(requestCode, resultCode, data);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_FITBIT_CODE, code);
    }

    @Override protected void restoreState(@NonNull Bundle savedState) {
        super.restoreState(savedState);
        code = savedState.getString(KEY_FITBIT_CODE);
    }

    @Override protected void onResume() {
        super.onResume();
        googleFitPresenter.onShown();
    }

    @Override protected void onStop() {
        if (sendingFitbitCodeSubs != null) {
            sendingFitbitCodeSubs.unsubscribe();
            sendingFitbitCodeSubs = null;
        }
        super.onStop();
    }

    @Override protected void onDestroy() {
        googleFitPresenter.detachView();
        Bus.unsubscribe(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) public void onEvent(GoogleFitAuthorizationEnabledEvent event) {
        uiGoogleFitSC.setChecked(userInteractor.isGoogleFitAuthEnabled());
    }

    @Subscribe(threadMode = ThreadMode.MAIN) public void onEvent(FitBitAuthorizationEnabled event){
        uiFitBitSC.setChecked(userInteractor.isFitBitAuthEnabled());
    }

}