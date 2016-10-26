package com.b_designworks.android.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.BuildConfig;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.login.functional_area.Area;
import com.b_designworks.android.login.functional_area.FunctionalToAreaCodeScreen;
import com.b_designworks.android.utils.Keyboard;
import com.b_designworks.android.utils.Rxs;
import com.b_designworks.android.utils.di.Injector;
import com.b_designworks.android.utils.network.RetrofitException;
import com.b_designworks.android.utils.ui.SimpleDialog;
import com.b_designworks.android.utils.ui.TextViews;
import com.b_designworks.android.utils.ui.UiInfo;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import rx.Subscription;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class EnterPhoneScreen extends BaseActivity {

    private static final int CODE_REQUEST_AREA = 1121;

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

    @SuppressLint("SetTextI18n") @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
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
        } else {
            uiPhone.setError(getString(R.string.registration_error_fill_phone));
        }
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