package com.b_designworks.android.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.DI;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.UserManager;
import com.b_designworks.android.utils.Bus;
import com.b_designworks.android.utils.Rxs;
import com.b_designworks.android.utils.network.ErrorUtils;
import com.b_designworks.android.utils.ui.TextViews;
import com.b_designworks.android.utils.ui.UiInfo;
import com.f2prateek.dart.InjectExtra;
import com.trello.rxlifecycle.ActivityEvent;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class VerifyScreen extends BaseActivity {

    private static final String RESULT_KEY_VERIFICATION_CODE = "verificationCode";
    private static final String ARG_KEY_PHONE                = "phone";
    private static final String KEY_USER_REGISTERED          = "userRegistered";
    private static final String KEY_PROGRESS_VISIBILITY      = "progressVisibility";

    public static Intent createIntent(Context context, @NonNull String phone) {
        Intent intent = new Intent(context, VerifyScreen.class);
        intent.putExtra(ARG_KEY_PHONE, phone);
        return intent;
    }

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_verify);
    }

    @InjectExtra(ARG_KEY_PHONE) String argPhone;

    @Bind(R.id.verification_code) EditText uiVerificadtionCode;
    @Bind(R.id.progress)          View     uiProgress;

    private UserManager userManager = DI.getInstance().getUserManager();
    private boolean userRegistered;

    @Nullable private Subscription verifyingCodeSubs;

    @SuppressWarnings("WrongConstant")
    @Override protected void restoreState(@NonNull Bundle savedState) {
        super.restoreState(savedState);
        userRegistered = savedState.getBoolean(KEY_USER_REGISTERED);
        uiProgress.setVisibility(savedState.getInt(KEY_PROGRESS_VISIBILITY));
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        if (savedState == null) {
            requestCode();
        }
    }

    @OnClick(R.id.submit) void onSubmitClick() {
        String verificadtionCode = TextViews.textOf(uiVerificadtionCode);
        if (!TextUtils.isEmpty(verificadtionCode)) {
            handleCode(verificadtionCode);
        } else {
            uiVerificadtionCode.setError(getString(R.string.registration_error_fill_code));
        }
    }

    @OnClick(R.id.resend) void onResendClick() {
        requestCode();
    }

    @Override protected void onResume() {
        super.onResume();
        Bus.subscribe(this);
    }

    @Subscribe public void onEvent(SmsCodeEvent event) {
        handleCode(event.getCode());
    }

    private void handleCode(@NonNull String code) {
        dismissProgressDialog();
        if (userRegistered) {
            login(code);
        } else {
            if (getCallingActivity() != null) {
                returnCodeToRegistrationScreen(code);
            } else {
                Navigator.registration(context(), code);
            }
        }
    }

    private void returnCodeToRegistrationScreen(@NonNull String code) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_KEY_VERIFICATION_CODE, code);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Nullable private ProgressDialog authorizeProgressDialog;

    @Nullable ProgressDialog progressDialog;
    @Nullable Subscription   sendingSubscription;

    private void requestCode() {
        if (sendingSubscription != null) return;

        progressDialog = ProgressDialog.show(context(), getString(R.string.loading), getString(R.string.loading_sending_request_for_code));
        sendingSubscription = userManager.requestCode(argPhone)
            .compose(bindUntilEvent(ActivityEvent.STOP))
            .compose(Rxs.doInBackgroundDeliverToUI())
            .doOnEach(i -> {
                dismissProgressDialog();
                sendingSubscription = null;
            })
            .subscribe(result -> {
                userRegistered = result.isPhoneRegistered();
                uiProgress.setVisibility(View.VISIBLE);
            }, ErrorUtils.handle(context()));
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override protected void onStop() {
        dismissAuthorizeDialog();
        dismissProgressDialog();
        super.onStop();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_USER_REGISTERED, userRegistered);
        outState.putInt(KEY_PROGRESS_VISIBILITY, uiProgress.getVisibility());
    }

    private void login(@NonNull String verifyCode) {
        if (verifyingCodeSubs != null && !verifyingCodeSubs.isUnsubscribed() || verifyingCodeSubs != null)
            return;
        authorizeProgressDialog = ProgressDialog.show(context(), getString(R.string.loading), getString(R.string.loading_sending_code));
        verifyingCodeSubs = userManager.verifyCode(verifyCode)
            .compose(Rxs.doInBackgroundDeliverToUI())
            .compose(bindUntilEvent(ActivityEvent.STOP))
            .doOnEach(i -> {
                verifyingCodeSubs = null;
                dismissAuthorizeDialog();
            })
            .subscribe(result -> Navigator.chat(context()), ErrorUtils.handle(context()));
    }

    private void dismissAuthorizeDialog() {
        if (authorizeProgressDialog != null) {
            authorizeProgressDialog.dismiss();
            authorizeProgressDialog = null;
        }
    }

    @Override protected void onPause() {
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

    public static String extractCodeFromResult(@NonNull Intent data) {
        return data.getExtras().getString(RESULT_KEY_VERIFICATION_CODE);
    }
}