package com.pairup.android.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.BuildConfig;
import android.text.InputType;
import android.util.Patterns;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.anjlab.android.iab.v3.TransactionDetails;
import com.f2prateek.dart.InjectExtra;
import com.pairup.android.BaseActivity;
import com.pairup.android.Navigator;
import com.pairup.android.R;
import com.pairup.android.UserInteractor;
import com.pairup.android.subscription.SubscriptionPresenter;
import com.pairup.android.subscription.SubscriptionView;
import com.pairup.android.utils.Keyboard;
import com.pairup.android.utils.Rxs;
import com.pairup.android.utils.Strings;
import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.network.CommonError;
import com.pairup.android.utils.network.ErrorUtils;
import com.pairup.android.utils.network.RetrofitException;
import com.pairup.android.utils.ui.SimpleDialog;
import com.pairup.android.utils.ui.UiInfo;
import com.trello.rxlifecycle.ActivityEvent;

import java.util.Random;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import rx.Subscription;

import static com.pairup.android.utils.ui.TextViews.textOf;

/**
 * Created by Ilya Eremin on 09.08.2016.
 */
public class RegistrationScreen extends BaseActivity implements SubscriptionView{

    private static final String ARG_KEY_VERIFICATION_CODE = "argVerificationCode";
    private static final String ARG_PHONE_NUMBER          = "argPhoneNumber";
    private static final String ARG_PHONE_CODE_ID         = "argPhoneCodeId";

    public static Intent createIntent(Context context, String phoneNumber,
                                      String verificationCode, String phoneCodeId) {
        Intent intent = new Intent(context, RegistrationScreen.class);
        intent.putExtra(ARG_KEY_VERIFICATION_CODE, verificationCode);
        intent.putExtra(ARG_PHONE_NUMBER, phoneNumber);
        intent.putExtra(ARG_PHONE_CODE_ID, phoneCodeId);
        return intent;
    }

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_registration).setTitleRes(R.string.title_start_trial).enableBackButton();
    }

    @InjectExtra(ARG_KEY_VERIFICATION_CODE) String argVerificationCode;
    @InjectExtra(ARG_PHONE_CODE_ID)         String argPhoneCodeId;
    @InjectExtra(ARG_PHONE_NUMBER)          String argPhoneNumber;

    @Inject UserInteractor        userInteractor;
    @Inject LoginFlowInteractor   loginFlowInteractor;
    @Inject SubscriptionPresenter subscriptionPresenter;

    @Bind(R.id.first_name) EditText uiFirstName;
    @Bind(R.id.last_name)  EditText uiLastName;
    @Bind(R.id.email)      EditText uiEmail;
    @Bind(R.id.phone)      EditText uiPhone;

    @Nullable private Subscription   progressSubs;
    @Nullable private ProgressDialog progressDialog;

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
        subscriptionPresenter.attachView(this, this);
        if (savedState == null) {
            if (BuildConfig.DEBUG) {
                fillFakeData();
            }
            if (loginFlowInteractor.userAlreadyFilledRegistration()) {
                uiFirstName.setText(loginFlowInteractor.getFirstName());
                uiLastName.setText(loginFlowInteractor.getLastName());
                uiEmail.setText(loginFlowInteractor.getEmail());
            }
        }
        uiPhone.setInputType(InputType.TYPE_NULL);
        uiPhone.setKeyListener(null);
        uiPhone.setText(argPhoneNumber);
    }

    @SuppressLint("SetTextI18n")
    private void fillFakeData() {
        int randomNumber = new Random().nextInt(10000);
        uiFirstName.setText("Test");
        uiLastName.setText("User" + randomNumber);
        uiEmail.setText("example" + randomNumber);
    }

    @OnClick(R.id.choose_your_plan) void onRegisterClick() {
        if (subscriptionPresenter.isSubscribed()) {
            tryRegistration();
        } else {
            subscriptionPresenter.showSubscriptionDialog();
        }
    }

    private void tryRegistration() {
        Keyboard.hide(this);
        if (progressSubs != null || !fieldVerificationPassed()) return;
        performRegistration();
    }

    private void performRegistration() {
        showRegistrationDialog();
        progressSubs = userInteractor.register(textOf(uiFirstName), textOf(uiLastName),
            textOf(uiEmail), argVerificationCode, textOf(uiPhone), argPhoneCodeId)
            .compose(bindUntilEvent(ActivityEvent.STOP))
            .compose(Rxs.doInBackgroundDeliverToUI())
            .doOnEach(i -> {
                progressSubs = null;
                dismissProgressDialog();
            })
            .subscribe(result -> {
                loginFlowInteractor.reset();
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
                                        loginFlowInteractor.saveRegistrationData(textOf(uiFirstName), textOf(uiLastName), textOf(uiEmail));
                                        finish();
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
            getString(R.string.loading_creating_account), true, true);
        progressDialog.setOnCancelListener(dialog -> {
            if (progressSubs != null && !progressSubs.isUnsubscribed()) {
                progressSubs.unsubscribe();
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
        return !hasError;
    }

    @OnEditorAction(R.id.email) boolean onEnterClick(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            tryRegistration();
            return true;
        }
        return false;
    }

    @Override public void onProductPurchased(String productId, TransactionDetails details) {
        Toast.makeText(this, R.string.subscription_owned_text, Toast.LENGTH_LONG).show();
    }

    @Override public void showSubscriptionDialog() {
        SimpleDialog.showList(this,
            getString(R.string.subscriptions),
            getResources().getStringArray(R.array.subscriptions),
            integer -> subscriptionPresenter.subscribe(integer));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            tryRegistration();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
