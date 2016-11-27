package com.pairup.android.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.pairup.android.BaseActivity;
import com.pairup.android.Navigator;
import com.pairup.android.R;
import com.pairup.android.UserInteractor;
import com.pairup.android.utils.Bus;
import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.network.ErrorUtils;
import com.pairup.android.utils.ui.SimpleDialog;
import com.pairup.android.utils.ui.UiInfo;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class VerifyScreen extends BaseActivity implements VerifyView {

    public static final String ARG_HAS_HBF_PROVIDER = "hasHbfProvider";

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_verify)
            .enableBackButton();
    }

    @Inject VerifyPresenter     verifyPresenter;
    @Inject LoginFlowInteractor loginFlowInteractor;
    @Inject UserInteractor      userInteractor;

    @Bind(R.id.hbf_logo) ImageView uiHbfLogo;

    private boolean hasHbfProvider;

    @SuppressWarnings("WrongConstant")
    @Override protected void restoreState(@NonNull Bundle savedState) {
        super.restoreState(savedState);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (!hasHbfProvider) {
            uiHbfLogo.setVisibility(View.GONE);
        }
        verifyPresenter.attachView(this);
        if (getIntent() != null) {
            handleCodeFromSms(getIntent());
        }
    }

    private void handleCodeFromSms(@NonNull Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            if (loginFlowInteractor.userEnteredPhone()) {
                String url = data.toString();
                String code = url.substring(url.length() - 4);
                verifyPresenter.handleSmsCode(code);
            } else {
                if (userInteractor.userLoggedIn()) {
                    Toast.makeText(this, R.string.warning_you_already_logged_in, Toast.LENGTH_SHORT).show();
                    Navigator.chat(context());
                    finish();
                } else {
                    SimpleDialog.withOkBtn(context(), R.string.error, R.string.error_no_accosiated_number, () -> {
                        Navigator.welcome(context());
                        finish();
                    });
                }
            }
        }
    }

    @Override protected void parseArguments(@NonNull Bundle extras) {
        hasHbfProvider = extras.getBoolean(ARG_HAS_HBF_PROVIDER, false);
    }

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleCodeFromSms(intent);
    }

    @OnClick(R.id.resend) void onResendClick() {
        verifyPresenter.sendCode();
    }

    @Override protected void onResume() {
        super.onResume();
        Bus.subscribe(this);
    }

    @Subscribe public void onEvent(SmsCodeEvent event) {
        verifyPresenter.handleSmsCode(event.getCode());
    }

    @Nullable private ProgressDialog authorizeProgressDialog;
    @Nullable private ProgressDialog requestVerificationCodeProgressDialog;

    @Override protected void onStop() {
        hideAuthProgressDialog();
        hideRequestVerificationProgressDialog();
        verifyPresenter.onShown();
        super.onStop();
    }

    @Override protected void onPause() {
        verifyPresenter.onHidden();
        Bus.unsubscribe(this);
        super.onPause();
    }

    @Override public void showRequestVerificationCodeProgressDialog() {
        requestVerificationCodeProgressDialog = ProgressDialog.show(context(), getString(R.string.loading), getString(R.string.loading_sending_request_for_code));
    }

    @Override public void showError(Throwable error) {
        ErrorUtils.handle(context(), error);
    }

    @Override public void hideRequestVerificationProgressDialog() {
        if (requestVerificationCodeProgressDialog != null) {
            requestVerificationCodeProgressDialog.dismiss();
            requestVerificationCodeProgressDialog = null;
        }
    }

    @Override public void showAuthorizationProgressDialog() {
        authorizeProgressDialog = ProgressDialog.show(context(), getString(R.string.loading), getString(R.string.loading_sending_code));
    }

    @Override public void hideAuthProgressDialog() {
        if (authorizeProgressDialog != null) {
            authorizeProgressDialog.dismiss();
            authorizeProgressDialog = null;
        }
    }

    @Override public void openChatScreen() {
        Navigator.chat(context());
    }

    @Override public void openTourScreen() {
        Navigator.tour(context());
    }

    @Override public void openRegistrationScreen(
        @NonNull String phone, @NonNull String code, @NonNull String phoneCodeId) {
        Navigator.registration(context(), phone, code, phoneCodeId);
    }

    @Override protected void onDestroy() {
        verifyPresenter.detachView();
        super.onDestroy();
    }
}