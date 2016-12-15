package com.pairup.android.login;

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
import com.pairup.android.utils.Areas;
import com.pairup.android.utils.Analytics;
import com.pairup.android.utils.Keyboard;
import com.pairup.android.utils.Rxs;
import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.network.ErrorUtils;
import com.pairup.android.utils.network.RetrofitException;
import com.pairup.android.utils.ui.SimpleDialog;
import com.pairup.android.utils.ui.TextViews;
import com.pairup.android.utils.ui.UiInfo;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import rx.Subscription;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class EnterPhoneScreen extends BaseActivity {

    public static final String ARG_ACCOUNT_VERIFICATION_TYPE = "account_verification_type";

    private static final int CODE_REQUEST_AREA = 1121;

    private AccountVerificationType accountVerificationType;

    @Bind(R.id.phone)     EditText uiPhone;
    @Bind(R.id.submit)    Button   uiSubmit;
    @Bind(R.id.area_code) EditText uiAreaCode;

    @Inject UserInteractor      userInteractor;
    @Inject LoginFlowInteractor loginFlowInteractor;

    private boolean hasHbfProvider;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_enter_phone)
            .setTitleRes(R.string.title_verification)
            .enableBackButton();
    }

    @Override protected void parseArguments(@NonNull Bundle extras) {
        accountVerificationType = (AccountVerificationType) extras.getSerializable(ARG_ACCOUNT_VERIFICATION_TYPE);
    }

    @SuppressLint("SetTextI18n") @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);

        Analytics.logScreenOpened(Analytics.EVENT_OPEN_ENTER_CODE_SCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (BuildConfig.DEBUG && savedState == null) {
            uiAreaCode.setText("+7");
            uiPhone.setText("9872947933");
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
            showProgress();
        }
    }

    @OnClick(R.id.submit) void onSubmitClick() {
        String phone = TextViews.textOf(uiPhone);
        String areaCode = getAreaCode();
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(areaCode)) {
            if (isCorrectAreaCode(areaCode)) {
                manageSubmit(areaCode, phone);
            } else {
                uiAreaCode.setError(getString(R.string.registration_error_fill_area_code));
                uiAreaCode.requestFocus();
            }
        } else {
            uiPhone.setError(getString(R.string.registration_error_fill_phone));
            uiPhone.requestFocus();
        }
    }

    private void manageSubmit(@NonNull String areaCode, @NonNull String phone) {
        Keyboard.hide(this);
        showProgress();
        if ("+61".equals(areaCode) && '0' == phone.charAt(0)) {
            phone = phone.substring(1, phone.length());
        }
        final String formattedPhone = phone;
        userInteractor.requestUserStatus(areaCode + formattedPhone)
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(result -> {
                boolean passed = false;
                hasHbfProvider = result.userHasHbfProvider();
                switch (accountVerificationType) {
                    case IS_REGISTERED:
                        passed = result.isPhoneRegistered();
                        break;
                    case IS_NOT_REGISTERED:
                        passed = !result.isPhoneRegistered();
                        break;
                    case HAS_PROVIDER:
                        passed = hasHbfProvider;
                        break;
                }
                if (passed) {
                    requestAuthorizationCode(areaCode, formattedPhone);
                } else {
                    showErrorDialog();
                }
            }, ErrorUtils.handle(this));
    }

    private String getAreaCode() {
        String areaCode = TextViews.textOf(uiAreaCode);
        return areaCode.startsWith("+") ? areaCode : ("+" + areaCode);
    }

    /** @param areaCode always has "+" in the head cause getAreaCode() method was called before*/
    private boolean isCorrectAreaCode(String areaCode) {
        String areaCodeWithoutPlus = areaCode.substring(1, areaCode.length());
        List<Area> areas = Areas.getAreas(this);
        for (Area area : areas) {
            if (areaCodeWithoutPlus.equals(area.getCode().trim()))
                return true;
        }
        return false;
    }

    private void showErrorDialog() {
        String errorMessage = getString(R.string.screen_enter_phone_error);
        switch (accountVerificationType) {
            case IS_REGISTERED:
                errorMessage = getString(R.string.screen_enter_phone_error_not_registered);
                break;
            case IS_NOT_REGISTERED:
                errorMessage = getString(R.string.screen_enter_phone_error_registered);
                break;
            case HAS_PROVIDER:
                errorMessage = getString(R.string.screen_enter_phone_error_has_no_provider);
                break;
        }
        SimpleDialog.show(context(), getString(R.string.error), errorMessage,
            getString(R.string.ok), () -> Navigator.welcome(this));
    }

    private void requestAuthorizationCode(@NonNull String areaCode, @NonNull String phone) {
        if (verifyNumberSubs != null) return;
        verifyNumberSubs = userInteractor.requestCode(areaCode + phone)
            .doOnTerminate(() -> {
                verifyNumberSubs = null;
                hideProgress();
            })
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(result -> {
                loginFlowInteractor.setPhoneCodeId(result.getPhoneCodeId());
                loginFlowInteractor.setPhoneRegistered(result.isPhoneRegistered());
                loginFlowInteractor.setPhoneNumber(areaCode + phone);

                loginFlowInteractor.setHasHbfProvider(hasHbfProvider);
                Navigator.verification(context());
            }, error -> {
                if (error instanceof RetrofitException) {
                    RetrofitException retrofitError = (RetrofitException) error;
                    if (retrofitError.getKind() == RetrofitException.Kind.NETWORK) {
                        SimpleDialog.networkProblem(context());
                    } else {
                        uiPhone.setError(getString(R.string.error_incorrect_phone));
                        uiPhone.requestFocus();
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

    private void showProgress() {
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