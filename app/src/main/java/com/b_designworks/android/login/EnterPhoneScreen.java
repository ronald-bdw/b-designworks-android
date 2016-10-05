package com.b_designworks.android.login;

import android.annotation.SuppressLint;
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
import com.b_designworks.android.login.functional_area.FunctionalToAreaCodeScreen;
import com.b_designworks.android.login.functional_area.Area;
import com.b_designworks.android.utils.ui.TextViews;
import com.b_designworks.android.utils.ui.UiInfo;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnEditorAction;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class EnterPhoneScreen extends BaseActivity {

    private static final int CODE_REQUEST_AREA = 1121;

    @Bind(R.id.phone)     EditText uiPhone;
    @Bind(R.id.submit)    Button   uiSubmit;
    @Bind(R.id.area_code) EditText uiAreaCode;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_enter_phone)
            .setTitleRes(R.string.title_verification)
            .enableBackButton();
    }

    @SuppressLint("SetTextI18n") @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
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

    @OnClick(R.id.submit) void onSubmitClick() {
        String phone = TextViews.textOf(uiPhone);
        String areaCode = TextViews.textOf(uiAreaCode);
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(areaCode)) {
            // TODO what if phone number has incorrect format
            Navigator.verification(context(), areaCode + phone);
        } else {
            uiPhone.setError(getString(R.string.registration_error_fill_phone));
        }
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