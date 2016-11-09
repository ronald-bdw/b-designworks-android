package com.pairup.android.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.pairup.android.BaseActivity;
import com.pairup.android.BuildConfig;
import com.pairup.android.Navigator;
import com.pairup.android.R;
import com.pairup.android.UserInteractor;
import com.pairup.android.login.functional_area.Area;
import com.pairup.android.login.functional_area.FunctionalToAreaCodeScreen;
import com.pairup.android.utils.Keyboard;
import com.pairup.android.utils.Rxs;
import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.network.ErrorUtils;
import com.pairup.android.utils.network.RetrofitException;
import com.pairup.android.utils.ui.SimpleDialog;
import com.pairup.android.utils.ui.TextViews;
import com.pairup.android.utils.ui.UiInfo;
import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import rx.Subscription;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class EnterPhoneScreen extends BaseActivity {

    public static final String NEED_CHECK_USER_EXTRA = "need_check_user_extra";

    private static final int CODE_REQUEST_AREA = 1121;

    private boolean shouldUserBeRegistered;

    @Bind(R.id.phone)     EditText uiPhone;
    @Bind(R.id.submit)    Button   uiSubmit;
    @Bind(R.id.area_code) EditText uiAreaCode;

    @Inject UserInteractor      userInteractor;
    @Inject LoginFlowInteractor loginFlowInteractor;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_enter_phone)
            .setTitleRes(R.string.title_verification)
            .enableBackButton();
    }

    @Override protected void parseArguments(@NonNull Bundle extras) {
        shouldUserBeRegistered = extras.getBoolean(NEED_CHECK_USER_EXTRA, false);
    }

    @SuppressLint("SetTextI18n") @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (BuildConfig.DEBUG && savedState == null) {
            uiAreaCode.setText("+7");
            uiPhone.setText("9625535458");
        }
    }

    @OnEditorAction(R.id.phone) boolean onEnterClick(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            onSubmitClick();
            return true;
        }
        return false;
    }

    @Nullable private Subscription   verifyNumberSubs;
    @Nullable private ProgressDialog progressDialog;

    @Override protected void onResume() {
        super.onResume();
        if (verifyNumberSubs != null && progressDialog == null) {
            showProgerss();
        }
    }

    @OnClick(R.id.submit) void onSubmitClick() {
        String phone = TextViews.textOf(uiPhone);
        String areaCode = TextViews.textOf(uiAreaCode);
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(areaCode)) {
            if (RxPermissions.getInstance(context()).isGranted(Manifest.permission.RECEIVE_SMS)) {
                manageSubmit(areaCode, phone);
            } else {
                SimpleDialog.show(context(), getString(R.string.warning_permission_needed),
                    getString(R.string.sms_permission_description), getString(R.string.ok), () ->
                        RxPermissions.getInstance(context()).request(Manifest.permission.RECEIVE_SMS)
                            .subscribe(isGranted -> manageSubmit(areaCode, phone)),
                    getString(R.string.no_thanks), () -> manageSubmit(areaCode, phone));
            }
        } else {
            uiPhone.setError(getString(R.string.registration_error_fill_phone));
        }
    }

    private void manageSubmit(String areaCode, String phone) {
        if (shouldUserBeRegistered) {
            checkUserExist(areaCode, phone);
        } else {
            requestAuthorizationCode(areaCode, phone);
        }
    }

    private void checkUserExist(String areaCode, String phone) {
        Keyboard.hide(this);
        showProgerss();
        userInteractor.requestUserStatus(areaCode + phone)
            .doOnTerminate(() -> hideProgress())
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(result -> {
                if (result.isPhoneRegistered()) {
                    requestAuthorizationCode(areaCode, phone);
                } else {
                    showErrorDialog();
                }
            }, ErrorUtils.handle(this));
    }

    private void showErrorDialog() {
        SimpleDialog.show(context(), getString(R.string.error), getString(R.string.screen_enter_phone_error_no_account),
            getString(R.string.ok), () -> finish());
    }

    private void requestAuthorizationCode(String areaCode, String phone) {
        Keyboard.hide(this);
        showProgerss();
        if (verifyNumberSubs != null) return;
        verifyNumberSubs = userInteractor.requestCode(areaCode + phone)
            .doOnTerminate(() -> {
                hideProgress();
                verifyNumberSubs = null;
            })
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(result -> {
                loginFlowInteractor.setPhoneCodeId(result.getPhoneCodeId());
                loginFlowInteractor.setPhoneRegistered(result.isPhoneRegistered());
                loginFlowInteractor.setPhoneNumber(areaCode + phone);
                Navigator.verification(context());
            }, error -> {
                if (error instanceof RetrofitException) {
                    RetrofitException retrofitError = (RetrofitException) error;
                    if (retrofitError.getKind() == RetrofitException.Kind.NETWORK) {
                        SimpleDialog.networkProblem(context());
                    } else {
                        uiPhone.setError(getString(R.string.error_incorrect_phone));
                    }
                }
            });
    }

    @Override protected void onStop() {
        super.onStop();
        hideProgress();
    }

    private void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void showProgerss() {
        progressDialog = ProgressDialog.show(context(), getString(R.string.loading), getString(R.string.progress_verifying_phone_number), true, true, dialog -> {
            if (verifyNumberSubs != null && !verifyNumberSubs.isUnsubscribed()) {
                verifyNumberSubs.unsubscribe();
                verifyNumberSubs = null;
            }
        });
    }

    @OnClick(R.id.area_code_btn) void onAreaBtnClick() {
        Navigator.areaCode(this, CODE_REQUEST_AREA);
    }

    @SuppressLint("SetTextI18n")
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Area area = data.getParcelableExtra(FunctionalToAreaCodeScreen.KEY_SELECTED_AREA);
            uiAreaCode.setText("+" + area.getCode().trim());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}