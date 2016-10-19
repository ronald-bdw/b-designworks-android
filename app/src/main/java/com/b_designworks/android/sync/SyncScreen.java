package com.b_designworks.android.sync;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Toast;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.Bus;
import com.b_designworks.android.utils.di.Injector;
import com.b_designworks.android.utils.network.ErrorUtils;
import com.b_designworks.android.utils.ui.SimpleDialog;
import com.b_designworks.android.utils.ui.UiInfo;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class SyncScreen extends BaseActivity implements GoogleFitView {

    @Bind(R.id.sync_container)           View         uiSyncContainer;
    @Bind(R.id.google_fit_switch_compat) SwitchCompat uiGoogleFitSC;
    @Bind(R.id.fitbit_switch_compat)     SwitchCompat uiFitBitSC;

    @Inject GoogleFitPresenter googleFitPresenter;

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
        Bus.subscribe(this);
        googleFitPresenter.attachView(this, this);
        uiGoogleFitSC.setChecked(googleFitPresenter.isAuthorized());
    }

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_sync).enableBackButton().setTitleRes(R.string.title_sync);
    }

    @OnClick(R.id.google_fit) void onGoogleFitClick() {
        if (googleFitPresenter.isAuthorized()) {
            googleFitPresenter.logout();
        } else {
            googleFitPresenter.startIntegrate(this);
        }
    }

    @OnClick(R.id.fitbit) void onFitbitClick() {
        Navigator.fitbit(this);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleFitPresenter.handleResponse(requestCode, resultCode, data);
    }

    @Override protected void onResume() {
        super.onResume();
        googleFitPresenter.onShown();
    }

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

    @Override public void enableIntegrationButton(boolean enabled) {
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

    @Override protected void onDestroy() {
        googleFitPresenter.detachView();
        Bus.unsubscribe(this);
        super.onDestroy();
    }

    @Subscribe public void onEvent(GoogleFitAuthorizationEnabledEvent event) {
        uiGoogleFitSC.setChecked(googleFitPresenter.isAuthorized());
    }

}