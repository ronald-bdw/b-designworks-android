package com.b_designworks.android.sync;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.BuildConfig;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.utils.di.Injector;
import com.b_designworks.android.utils.network.ErrorUtils;
import com.b_designworks.android.utils.ui.SimpleLoadingDialog;
import com.b_designworks.android.utils.ui.UiInfo;

import javax.inject.Inject;

import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by Ilya Eremin on 9/26/16.
 */
public class FitbitScreen extends BaseActivity implements FitbitView {

    private static final String GETTING_CODE_URL = "https://www.fitbit.com/oauth2/" +
        "authorize?response_type=code" +
        "&client_id=" + BuildConfig.FITBIT_APP_ID +
//        "&redirect_uri=pearup%3A%2F%2Fpearup.com"+
        "&scope=activity";

    private static final String KEY_FITBIT_CODE = "fitbitCode";

    @Inject UserInteractor userInteractor;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_fitbit);
    }

    @Nullable String          code;
    @Nullable Subscription    sendingFitbitCodeSubs;
    private   FitbitPresenter fitbitPresenter;

    @Override protected void restoreState(@NonNull Bundle savedState) {
        super.restoreState(savedState);
        code = savedState.getString(KEY_FITBIT_CODE);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
        fitbitPresenter = new FitbitPresenter(this, userInteractor);
        Intent intent = getIntent();
        handleIntent(intent);
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

    @Override public void dismissSendingFitbitCodeProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override public void fitbitSuccessfullyIntegrated() {
        Toast.makeText(context(), R.string.fitbit_integration_successful, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override public void onError(Throwable error) {
        ErrorUtils.handle(context(), error);
    }

    @Nullable private ProgressDialog progressDialog;

    @Override
    public void showSendingFitbitCodeProgress() {
        progressDialog = SimpleLoadingDialog.show(context(), getString(R.string.sending_fitbit_code), () -> {
            if (sendingFitbitCodeSubs != null) {
                sendingFitbitCodeSubs.unsubscribe();
                sendingFitbitCodeSubs = null;
            }
        });
    }

    @Override protected void onStop() {
        if (sendingFitbitCodeSubs != null) {
            sendingFitbitCodeSubs.unsubscribe();
            sendingFitbitCodeSubs = null;
        }
        super.onStop();
    }

    @OnClick(R.id.start_integration) void onStartIntegrationClick() {
        Navigator.openUrl(context(), GETTING_CODE_URL);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_FITBIT_CODE, code);
    }
}