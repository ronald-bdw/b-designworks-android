package com.b_designworks.android.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.Bus;
import com.b_designworks.android.utils.di.Injector;
import com.b_designworks.android.utils.network.ErrorUtils;
import com.b_designworks.android.utils.ui.TextViews;
import com.b_designworks.android.utils.ui.UiInfo;
import com.f2prateek.dart.InjectExtra;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class VerifyScreen extends BaseActivity implements VerifyView {

    private static final String ARG_KEY_PHONE = "phone";

    private static final String KEY_USER_REGISTERED     = "userRegistered";
    private static final String KEY_PROGRESS_VISIBILITY = "progressVisibility";

    private static final String RESULT_KEY_VERIFICATION_CODE = "verificationCode";
    private static final String RESULT_KEY_PHONE_CODE_ID     = "phoneCodeId";
    private static final String RESULT_KEY_PHONE_NUMBER      = "phoneNumber";

    public static String extractCodeFromResult(@NonNull Intent data) {
        return data.getExtras().getString(RESULT_KEY_VERIFICATION_CODE);
    }

    public static String extractPhoneCodeIdFromResult(@NonNull Intent data) {
        return data.getExtras().getString(RESULT_KEY_PHONE_CODE_ID);
    }

    public static Intent createIntent(Context context, @NonNull String phone) {
        Intent intent = new Intent(context, VerifyScreen.class);
        intent.putExtra(ARG_KEY_PHONE, phone);
        return intent;
    }

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_verify);
    }

    @Inject VerifyPresenter verifyPresenter;

    @InjectExtra(ARG_KEY_PHONE) String argPhone;

    @Bind(R.id.verification_code) EditText uiVerificadtionCode;
    @Bind(R.id.progress)          View     uiWaitingForSms;

    private boolean userRegistered;

    @SuppressWarnings("WrongConstant")
    @Override protected void restoreState(@NonNull Bundle savedState) {
        super.restoreState(savedState);
        userRegistered = savedState.getBoolean(KEY_USER_REGISTERED);
        uiWaitingForSms.setVisibility(savedState.getInt(KEY_PROGRESS_VISIBILITY));
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
        verifyPresenter.attachView(this);
        if (savedState == null) {
            verifyPresenter.requestCode(argPhone);
        }
    }

    @OnClick(R.id.submit) void onSubmitClick() {
        verifyPresenter.handleSmsCode(TextViews.textOf(uiVerificadtionCode));
    }

    @OnClick(R.id.resend) void onResendClick() {
        verifyPresenter.requestCode(argPhone);
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

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_USER_REGISTERED, userRegistered);
        outState.putInt(KEY_PROGRESS_VISIBILITY, uiWaitingForSms.getVisibility());
    }

    @Override protected void onPause() {
        verifyPresenter.onHidden();
        Bus.unsubscribe(this);
        super.onPause();
    }

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            Bus.event(new SmsCodeEvent("Event triggers success sms verification"));
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override public void showRequestVerificationCodeProgressDialog() {
        requestVerificationCodeProgressDialog = ProgressDialog.show(context(), getString(R.string.loading), getString(R.string.loading_sending_request_for_code));
    }

    @Override public void showWaitingForSmsProgress() {
        uiWaitingForSms.setVisibility(View.VISIBLE);
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

    @Override public void showVerificationCodeError() {
        uiVerificadtionCode.setError(getString(R.string.registration_error_fill_code));
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

    @OnEditorAction(R.id.verification_code) boolean onEnterClick(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onSubmitClick();
            return true;
        }
        return false;
    }

    @Override public void openChatScreen() {
        Navigator.chat(context());
    }

    @Override public void openRegistrationScreen(
        @NonNull String code, @NonNull String phoneNumber, @NonNull String phoneCodeId) {
        if (getCallingActivity() != null) {
            returnCodeToRegistrationScreen(code, phoneNumber, phoneCodeId);
        } else {
            Navigator.registration(context(), code, phoneNumber, phoneCodeId);
        }
    }

    private void returnCodeToRegistrationScreen(@NonNull String code,
                                                @NonNull String phoneCodeId,
                                                @NonNull String phoneNumber) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_KEY_VERIFICATION_CODE, code);
        intent.putExtra(RESULT_KEY_PHONE_CODE_ID, phoneCodeId);
        intent.putExtra(RESULT_KEY_PHONE_NUMBER, phoneNumber);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override protected void onDestroy() {
        verifyPresenter.detachView();
        super.onDestroy();
    }
}