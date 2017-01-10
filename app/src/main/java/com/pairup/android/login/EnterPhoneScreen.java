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
import com.pairup.android.login.functional_area.Area;
import com.pairup.android.login.functional_area.FunctionalToAreaCodeScreen;
import com.pairup.android.utils.Analytics;
import com.pairup.android.utils.Keyboard;
import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.network.ErrorUtils;
import com.pairup.android.utils.ui.SimpleDialog;
import com.pairup.android.utils.ui.TextViews;
import com.pairup.android.utils.ui.UiInfo;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class EnterPhoneScreen extends BaseActivity implements EnterPhoneView {

    public static final String ARG_ACCOUNT_VERIFICATION_TYPE = "account_verification_type";
    public static final String ARG_PROVIDER_NAME = "provider_name";

    private static final int CODE_REQUEST_AREA = 1121;

    @Inject EnterPhonePresenter enterPhonePresenter;

    @Bind(R.id.phone)     EditText uiPhone;
    @Bind(R.id.area_code) EditText uiAreaCode;
    @Bind(R.id.submit)    Button   uiSubmit;

    @Nullable private ProgressDialog progressDialog;

    private AccountVerificationType accountVerificationType;
    @Nullable private String        providerName;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_enter_phone)
            .setTitleRes(R.string.title_verification)
            .enableBackButton();
    }

    @Override protected void parseArguments(@NonNull Bundle extras) {
        accountVerificationType = (AccountVerificationType) extras
            .getSerializable(ARG_ACCOUNT_VERIFICATION_TYPE);
        providerName = extras.getString(ARG_PROVIDER_NAME);
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

    @Override protected void onResume() {
        super.onResume();
        if (enterPhonePresenter.isVerifyNumberSubsNotNull() && progressDialog == null) {
            showProgress();
        }
    }

    @OnClick(R.id.submit) void onSubmitClick() {
        String phone = TextViews.textOf(uiPhone);
        String areaCode = enterPhonePresenter.getAreaCode(TextViews.textOf(uiAreaCode));
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(areaCode)) {
            if (enterPhonePresenter.isCorrectAreaCode(areaCode, this)) {
                enterPhonePresenter.manageSubmit(areaCode, phone,
                        accountVerificationType, providerName);
            } else {
                uiAreaCode.setError(getString(R.string.registration_error_fill_area_code));
                uiAreaCode.requestFocus();
            }
        } else {
            uiPhone.setError(getString(R.string.registration_error_fill_phone));
            uiPhone.requestFocus();
        }
    }

    @Override
    public void showErrorDialog() {
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
            default:
                break;
        }
        SimpleDialog.show(context(), getString(R.string.error), errorMessage,
            getString(R.string.ok), () -> Navigator.welcome(this));
    }

    @Override protected void onStop() {
        super.onStop();
        hideProgress();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void openVerificationScreen() {
        Navigator.verification(context());
    }

    @Override
    public void showDialogNetworkProblem() {
        SimpleDialog.networkProblem(context());
    }

    @Override
    public void showPhoneError() {
        uiPhone.setError(getString(R.string.error_incorrect_phone));
        uiPhone.requestFocus();
    }

    @Override
    public void handleError() {
        ErrorUtils.handle(this);
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(context(), getString(R.string.loading),
            getString(R.string.progress_verifying_phone_number), true, true, dialog -> {
                if (enterPhonePresenter.isVerifyNumberSubsNotNull() &&
                        enterPhonePresenter.isSubscribe()) {
                    enterPhonePresenter.unsubscribeSubs();
                }
            });
    }

    @Override
    public void hideKeyboard() {
        Keyboard.hide(this);
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