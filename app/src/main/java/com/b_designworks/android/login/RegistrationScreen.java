package com.b_designworks.android.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.widget.EditText;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.utils.Rxs;
import com.b_designworks.android.utils.Strings;
import com.b_designworks.android.utils.di.Injector;
import com.b_designworks.android.utils.network.CommonError;
import com.b_designworks.android.utils.network.ErrorUtils;
import com.b_designworks.android.utils.network.RetrofitException;
import com.b_designworks.android.utils.ui.SimpleOkDialog;
import com.b_designworks.android.utils.ui.UiInfo;
import com.f2prateek.dart.InjectExtra;
import com.f2prateek.dart.Optional;
import com.trello.rxlifecycle.ActivityEvent;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscription;

import static com.b_designworks.android.utils.ui.TextViews.textOf;

/**
 * Created by Ilya Eremin on 09.08.2016.
 */
public class RegistrationScreen extends BaseActivity {

    public static final int RESULT_KEY_FOR_VERIFYING = 5544;

    private static final String ARG_KEY_VERIFICATION_CODE = "argVerificationCode";
    private static final String ARG_PHONE_NUMBER          = "argPhoneNumber";
    private static final String ARG_PHONE_CODE_ID         = "argPhoneCodeId";

    private static final String KEY_RETURNED_VERIFICATION_CODE = "returnedVerificationCode";

    public static Intent createIntent(Context context, String verificationCode, String phoneNumber, String phoneCodeId) {
        Intent intent = new Intent(context, RegistrationScreen.class);
        intent.putExtra(ARG_KEY_VERIFICATION_CODE, verificationCode);
        intent.putExtra(ARG_PHONE_NUMBER, phoneNumber);
        intent.putExtra(ARG_PHONE_CODE_ID, phoneCodeId);
        return intent;
    }

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_registration).setTitleRes(R.string.title_start_trial).enableBackButton();
    }

    @Inject UserInteractor userInteractor;

    private String returnedVerificationCode;
    private String returnedPhoneCodeId;

    @Optional @InjectExtra(ARG_KEY_VERIFICATION_CODE) String argVerificationCode;
    @InjectExtra(ARG_PHONE_NUMBER)                    String argPhoneNumber;
    @InjectExtra(ARG_PHONE_CODE_ID)                   String argPhoneCodeId;

    @Bind(R.id.first_name) EditText uiFirstName;
    @Bind(R.id.last_name)  EditText uiLastName;
    @Bind(R.id.email)      EditText uiEmail;
    @Bind(R.id.phone)      EditText uiPhone;

    @Nullable private Subscription   progressSubs;
    @Nullable private ProgressDialog progressDialog;

    @Override protected void restoreState(@NonNull Bundle savedState) {
        super.restoreState(savedState);
        returnedVerificationCode = savedState.getString(KEY_RETURNED_VERIFICATION_CODE);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
        if (savedState == null) {
            uiPhone.setText(argPhoneNumber);
        }
    }

    @Override protected void onResume() {
        super.onResume();
        if (returnedVerificationCode != null) {
            performRegistration(returnedVerificationCode);
        }
    }

    @OnClick(R.id.choose_your_plan) void onRegisterClick() {
        if (progressSubs != null || !fieldVerificationPassed()) return;
        if (getVerificationCode() != null) {
            performRegistration(getVerificationCode());
        } else {
            Navigator.verifyAndReturnCode(this, textOf(uiPhone));
        }
    }

    @Nullable private String getVerificationCode() {
        return returnedVerificationCode == null ? argVerificationCode : returnedVerificationCode;
    }

    private void performRegistration(@NonNull String verificationCode) {
        progressDialog = ProgressDialog.show(context(), getString(R.string.loading), getString(R.string.loading_sending_code));
        progressSubs = userInteractor.register(textOf(uiFirstName), textOf(uiLastName), textOf(uiEmail), verificationCode, argPhoneNumber, argPhoneCodeId)
            .compose(bindUntilEvent(ActivityEvent.STOP))
            .compose(Rxs.doInBackgroundDeliverToUI())
            .doOnEach(i -> {
                returnedVerificationCode = null;
                progressSubs = null;
                dismissProgressDialog();
            })
            .subscribe(result -> {
                Navigator.chat(context());
            }, error -> {
                if (error instanceof RetrofitException) {
                    CommonError parsedError = ((RetrofitException) error).getErrorBodyAs(CommonError.class);
                    if (parsedError != null && parsedError.getValidations() != null) {
                        for (String key : parsedError.getValidations().keySet()) {
                            String errorMsg = Strings.listToString(parsedError.getValidations().get(key));
                            if (key.equals("sms_code")) {
                                SimpleOkDialog.show(context(), "Incorrect verification code, try again");
                                finish();
//                                uiCode.setError(Strings.listToString(parsedError.getValidations().get(key)));
                            } else if (key.equals("email")) {
                                uiEmail.setError(errorMsg);
                            }
                        }
                    }
                    if (((RetrofitException) error).getKind() == RetrofitException.Kind.NETWORK) {
                        SimpleOkDialog.networkProblem(context());
                    }
                } else {
                    ErrorUtils.handle(context(), error);
                }
            });
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override protected void onStop() {
        dismissProgressDialog();
        super.onStop();
    }

    private boolean fieldVerificationPassed() {
        boolean hasError = false;
        if (textOf(uiFirstName).isEmpty()) {
            hasError = true;
            uiFirstName.setError(getString(R.string.registration_error_fill_first_name));
        }
        if (textOf(uiLastName).isEmpty()) {
            hasError = true;
            uiLastName.setError(getString(R.string.registration_error_fill_last_name));
        }
        if (textOf(uiEmail).isEmpty()) {
            hasError = true;
            uiEmail.setError(getString(R.string.error_empty_email));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(textOf(uiEmail)).matches()) {
            hasError = true;
            uiEmail.setError(getString(R.string.error_incorrect_email));
        }
        if (textOf(uiPhone).isEmpty()) {
            hasError = true;
            uiPhone.setError(getString(R.string.registration_error_fill_phone));
        }
        return !hasError;
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_RETURNED_VERIFICATION_CODE, returnedVerificationCode);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == RESULT_KEY_FOR_VERIFYING) {
            returnedVerificationCode = VerifyScreen.extractCodeFromResult(data);
            returnedPhoneCodeId = VerifyScreen.extractPhoneCodeIdFromResult(data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
