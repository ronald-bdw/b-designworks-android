package com.b_designworks.android.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.BuildConfig;
import android.util.Patterns;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.utils.Keyboard;
import com.b_designworks.android.utils.Rxs;
import com.b_designworks.android.utils.Strings;
import com.b_designworks.android.utils.di.Injector;
import com.b_designworks.android.utils.network.CommonError;
import com.b_designworks.android.utils.network.ErrorUtils;
import com.b_designworks.android.utils.network.RetrofitException;
import com.b_designworks.android.utils.ui.SimpleDialog;
import com.b_designworks.android.utils.ui.UiInfo;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.Random;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnEditorAction;
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
    private static final String KEY_PHONE_CODE_ID              = "phoneCodeId";

    private ProgressDialog phoneVerificationDialog;

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

    @Nullable private String verifiedPhoneNumber;
    @Nullable private String returnedVerificationCode;
    @Nullable private String returnedPhoneCodeId;

    @Bind(R.id.first_name) EditText uiFirstName;
    @Bind(R.id.last_name)  EditText uiLastName;
    @Bind(R.id.email)      EditText uiEmail;
    @Bind(R.id.phone)      EditText uiPhone;

    @Nullable private Subscription   progressSubs;
    @Nullable private ProgressDialog progressDialog;

    @Override protected void restoreState(@NonNull Bundle savedState) {
        super.restoreState(savedState);
        returnedVerificationCode = savedState.getString(KEY_RETURNED_VERIFICATION_CODE);
        returnedPhoneCodeId = savedState.getString(KEY_PHONE_CODE_ID);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
        if (savedState == null) {
            if (BuildConfig.DEBUG) {
                fillFakeData();
            }
        }
        uiPhone.setInputType(InputType.TYPE_NULL);
        uiPhone.setKeyListener(null);
    }

    @SuppressLint("SetTextI18n")
    private void fillFakeData() {
        int randomNumber = new Random().nextInt(10000);
        uiFirstName.setText("Test");
        uiLastName.setText("User" + randomNumber);
        uiEmail.setText("example" + randomNumber);
        uiPhone.setText("+796255" + randomNumber);
    }

    @Override protected void onResume() {
        super.onResume();
        if (returnedVerificationCode != null && textOf(uiPhone).equals(verifiedPhoneNumber)) {
            performRegistration(returnedVerificationCode);
        }
    }

    @OnClick(R.id.choose_your_plan) void onRegisterClick() {
        tryRegistration();
    }

    private void tryRegistration() {
        Keyboard.hide(this);
        if (progressSubs != null || !fieldVerificationPassed()) return;
        if (returnedVerificationCode != null && textOf(uiPhone).equals(verifiedPhoneNumber)) {
            performRegistration(returnedVerificationCode);
        } else {
            verifyPhone();
        }
    }

    private void performRegistration(@NonNull String verificationCode) {
        showRegistrationDialog();
        progressSubs = userInteractor.register(textOf(uiFirstName), textOf(uiLastName),
            textOf(uiEmail), verificationCode, textOf(uiPhone), returnedPhoneCodeId)
            .compose(bindUntilEvent(ActivityEvent.STOP))
            .compose(Rxs.doInBackgroundDeliverToUI())
            .doOnEach(i -> {
                progressSubs = null;
                dismissProgressDialog();
            })
            .subscribe(result -> {
                Navigator.tour(context());
            }, error -> {
                if (error instanceof RetrofitException) {
                    CommonError parsedError = ((RetrofitException) error).getErrorBodyAs(CommonError.class);
                    if (parsedError != null && parsedError.getValidations() != null) {
                        for (String key : parsedError.getValidations().keySet()) {
                            String errorMsg = Strings.listToString(parsedError.getValidations().get(key));
                            if (key.equals("sms_code")) {
                                SimpleDialog.show(context(), null, getString(R.string.error_incorrect_verification_code),
                                    getString(R.string.try_again), () -> {
                                        Navigator.verifyAndReturnCode(this, textOf(uiPhone), returnedPhoneCodeId, false);
                                    });
                            } else if (key.equals("email")) {
                                uiEmail.setError(errorMsg);
                                uiEmail.requestFocus();
                            }
                        }
                    }
                    if (((RetrofitException) error).getKind() == RetrofitException.Kind.NETWORK) {
                        SimpleDialog.networkProblem(context());
                    }
                } else {
                    ErrorUtils.handle(context(), error);
                }
            });
    }

    private void showRegistrationDialog() {
        progressDialog = ProgressDialog.show(context(), getString(R.string.loading),
            getString(R.string.loading_sending_code), true, true);
        progressDialog.setOnCancelListener(dialog -> {
            if (progressSubs != null && !progressSubs.isUnsubscribed()) {
                progressSubs.unsubscribe();
            }
        });
    }

    private void verifyPhone() {
        showPhoneVerificationDialog();
        userInteractor.requestCode(textOf(uiPhone))
            .doOnTerminate(() -> {
                if (phoneVerificationDialog != null) {
                    phoneVerificationDialog.dismiss();
                    phoneVerificationDialog = null;
                }
            })
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(result -> {
                if (result.isPhoneRegistered()) {
                    uiPhone.setError(getString(R.string.error_phone_taken));
                } else {
                    Navigator.verifyAndReturnCode(this, textOf(uiPhone), result.getPhoneCodeId(), false);
                }
            }, error -> {
                if (error instanceof RetrofitException) {
                    CommonError parsedError = ((RetrofitException) error).getErrorBodyAs(CommonError.class);
                    if (parsedError != null && parsedError.getValidations() != null) {
                        if (parsedError.getMessage().contains("number")) {
                            uiPhone.setError(getString(R.string.error_incorrect_phone));
                        }
                    }
                } else {
                    ErrorUtils.handle(context(), error);
                }
            });
    }

    private void showPhoneVerificationDialog() {
        phoneVerificationDialog = ProgressDialog.show(context(),
            getString(R.string.loading),
            getString(R.string.progress_verifying_phone_number));
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

    @OnEditorAction(R.id.phone) boolean onEnterClick(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            tryRegistration();
            return true;
        }
        return false;
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_RETURNED_VERIFICATION_CODE, returnedVerificationCode);
        outState.putString(KEY_PHONE_CODE_ID, returnedPhoneCodeId);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == RESULT_KEY_FOR_VERIFYING) {
            returnedVerificationCode = VerifyScreen.extractCodeFromResult(data);
            returnedPhoneCodeId = VerifyScreen.extractPhoneCodeIdFromResult(data);
            verifiedPhoneNumber = VerifyScreen.extractPhone(data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
