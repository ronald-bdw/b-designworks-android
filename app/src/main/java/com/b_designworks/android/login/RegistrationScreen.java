package com.b_designworks.android.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.BuildConfig;
import com.b_designworks.android.DI;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.Rxs;
import com.b_designworks.android.utils.network.ErrorUtils;
import com.b_designworks.android.utils.ui.UiInfo;
import com.f2prateek.dart.InjectExtra;

import butterknife.Bind;
import butterknife.OnClick;

import static com.b_designworks.android.utils.ui.TextViews.textOf;

/**
 * Created by Ilya Eremin on 09.08.2016.
 */
public class RegistrationScreen extends BaseActivity {

    private static final String ARG_PHONE         = "phone";
    private static final String ARG_PHONE_CODE_ID = "phoneCodeId";

    public static Intent createIntent(@NonNull Context context,
                                      @NonNull String phone,
                                      @NonNull String phoneCodeId) {
        Intent intent = new Intent(context, RegistrationScreen.class);
        intent.putExtra(ARG_PHONE, phone);
        intent.putExtra(ARG_PHONE_CODE_ID, phoneCodeId);
        return intent;
    }

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_registration);
    }

    @InjectExtra(ARG_PHONE)         String argPhoneNumber;
    @InjectExtra(ARG_PHONE_CODE_ID) String argPhoneCodeId;

    @Bind(R.id.first_name) EditText uiFirstName;
    @Bind(R.id.last_name)  EditText uiLastName;
    @Bind(R.id.email)      EditText uiEmail;
    @Bind(R.id.code)       EditText uiCode;

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        if (BuildConfig.DEBUG) {
            uiCode.setText("1234");
        }
    }

    @OnClick(R.id.register) void onRegisterClick() {
        DI.getInstance().getApi().register(textOf(uiFirstName), textOf(uiLastName),
            textOf(uiEmail), textOf(uiCode), argPhoneNumber, argPhoneCodeId)
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(result -> {

            }, ErrorUtils.onError());
    }

}
