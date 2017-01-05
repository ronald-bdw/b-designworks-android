package com.pairup.android.login;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pairup.android.BaseActivity;
import com.pairup.android.Navigator;
import com.pairup.android.R;
import com.pairup.android.UserInteractor;
import com.pairup.android.login.models.ProviderType;
import com.pairup.android.utils.Analytics;
import com.pairup.android.utils.Keyboard;
import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.network.ErrorUtils;
import com.pairup.android.utils.ui.TextViews;
import com.pairup.android.utils.ui.UiInfo;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class VerifyScreen extends BaseActivity implements VerifyView {

    @Inject VerifyPresenter     verifyPresenter;
    @Inject LoginFlowInteractor loginFlowInteractor;
    @Inject UserInteractor      userInteractor;

    @Bind(R.id.provider_logo)     ImageView uiProviderLogo;
    @Bind(R.id.verification_code) EditText  uiVerificationCode;

    @Nullable private ProgressDialog authorizeProgressDialog;
    @Nullable private ProgressDialog requestVerificationCodeProgressDialog;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_verify)
            .enableBackButton();
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);

        Analytics.logScreenOpened(Analytics.EVENT_OPEN_VERIFY_SCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ProviderType provider = loginFlowInteractor.getProvider();
        if (provider != null && provider.getVerifyLogo() != 0) {
            Glide.with(this).load(provider.getVerifyLogo()).into(uiProviderLogo);
        } else {
            uiProviderLogo.setVisibility(View.GONE);
        }
        verifyPresenter.attachView(this);
    }

    @OnClick(R.id.submit) void onSubmitClick() {
        Keyboard.hide(this);
        verifyPresenter.handleSmsCode(TextViews.textOf(uiVerificationCode));
    }

    @OnClick(R.id.resend) void onResendClick() {
        verifyPresenter.sendCode();
    }

    @Override public void showRequestVerificationCodeProgressDialog() {
        requestVerificationCodeProgressDialog = ProgressDialog.show(context(),
            getString(R.string.loading), getString(R.string.loading_sending_request_for_code));
    }

    @Override public void showError(Throwable error) {
        ErrorUtils.handle(context(), error);
    }

    @Override public void showVerificationCodeError() {
        uiVerificationCode.setError(getString(R.string.registration_error_fill_code));
    }

    @Override public void hideRequestVerificationProgressDialog() {
        if (requestVerificationCodeProgressDialog != null) {
            requestVerificationCodeProgressDialog.dismiss();
            requestVerificationCodeProgressDialog = null;
        }
    }

    @Override public void showAuthorizationProgressDialog() {
        authorizeProgressDialog = ProgressDialog.show(context(),
            getString(R.string.loading), getString(R.string.loading_sending_code));
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

    @OnEditorAction(R.id.verification_code) boolean onEnterClick(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onSubmitClick();
            return true;
        }
        return false;
    }
}