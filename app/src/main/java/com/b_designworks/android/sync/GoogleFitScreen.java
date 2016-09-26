package com.b_designworks.android.sync;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.di.Injector;
import com.b_designworks.android.utils.network.ErrorUtils;
import com.b_designworks.android.utils.ui.SimpleOkDialog;
import com.b_designworks.android.utils.ui.UiInfo;
import com.google.android.gms.common.ConnectionResult;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 24.08.2016.
 */
public class GoogleFitScreen extends BaseActivity implements GoogleFitView {

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_google_fit);
    }

    @Bind(R.id.start_integration) View uiStartIntegration;

    @Inject GoogleFitPresenter googleFitPresenter;

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
        googleFitPresenter.attachView(this);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleFitPresenter.handleResponse(requestCode, resultCode, data);
    }

    @OnClick(R.id.start_integration) void onStartIntergrationClick() {
        googleFitPresenter.startIntegrate(this);
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
        Toast.makeText(GoogleFitScreen.this, "Exception while connecting to Google Play services: " +
            result.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    @Override public void enableIntegrationButton(boolean enabled) {
        uiStartIntegration.setEnabled(enabled);
    }

    @Override public void showInternetConnectionError() {
        SimpleOkDialog.networkProblem(context());
    }

    @Override public void showGoogleServiceDisconected() {

    }

    @Override public void onError(Throwable error) {
        ErrorUtils.handle(context(), error);
    }

    @Override protected void onDestroy() {
        googleFitPresenter.detachView();
        super.onDestroy();
    }
}
