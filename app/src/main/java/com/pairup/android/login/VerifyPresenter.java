package com.pairup.android.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pairup.android.UserInteractor;
import com.pairup.android.utils.Rxs;
import com.pairup.android.utils.Strings;

import rx.Subscription;

/**
 * Created by Ilya Eremin on 9/21/16.
 */
public class VerifyPresenter {

    private final UserInteractor      userInteractor;
    private final LoginFlowInteractor loginFlowInteractor;

    @Nullable private VerifyView   view;
    @Nullable private Subscription requestingSmsCodeSubs;
    @Nullable private Subscription verifyingCodeSubs;

    public VerifyPresenter(@NonNull UserInteractor userInteractor,
                           @NonNull LoginFlowInteractor loginFlowInteractor) {
        this.userInteractor = userInteractor;
        this.loginFlowInteractor = loginFlowInteractor;
    }

    public void attachView(@NonNull VerifyView view) {
        this.view = view;
    }

    public void sendCode() {
        String phoneNumber = loginFlowInteractor.getPhoneNumber();
        if (view != null) {
            view.showRequestVerificationCodeProgressDialog();
        }

        if (requestingSmsCodeSubs != null) return;

        requestingSmsCodeSubs = userInteractor.requestCode(phoneNumber)
            .doOnTerminate(() -> {
                view.hideRequestVerificationProgressDialog();
                requestingSmsCodeSubs = null;
            })
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(result -> {
                loginFlowInteractor.setPhoneCodeId(result.getPhoneCodeId());
                loginFlowInteractor.setPhoneRegistered(result.isPhoneRegistered());
            }, view::showError);
    }

    public void handleSmsCode(String verificationCode) {
        if (!Strings.isEmpty(verificationCode)) {
            if (loginFlowInteractor.isUserRegistered()) {
                login(verificationCode);
            } else {
                checkVerificationCodeAndGoRegister(verificationCode);
            }
        } else {
            if (view != null) {
                view.showVerificationCodeError();
            }
        }
    }

    private void checkVerificationCodeAndGoRegister(@NonNull String verificationCode) {
        if (verifyingCodeSubs != null && !verifyingCodeSubs.isUnsubscribed())
            return;
        if (view != null) {
            view.showAuthorizationProgressDialog();
        }
        verifyingCodeSubs = userInteractor
            .checkVerificationNumber(loginFlowInteractor.getPhoneCodeId(), verificationCode)
            .doOnTerminate(() -> {
                verifyingCodeSubs = null;
                if (view != null) {
                    view.hideAuthProgressDialog();
                }
            })
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(result -> {
                if (view != null) {
                    view.openRegistrationScreen(loginFlowInteractor.getPhoneNumber(),
                        verificationCode, loginFlowInteractor.getPhoneCodeId());
                }
            }, error -> {
                if (view != null) {
                    view.showError(error);
                }
            });
    }

    private void login(@NonNull String verifyCode) {
        if (verifyingCodeSubs != null && !verifyingCodeSubs.isUnsubscribed())
            return;
        if (view != null) {
            view.showAuthorizationProgressDialog();
        }
        String phoneNumber = loginFlowInteractor.getPhoneNumber();
        String phoneCodeId = loginFlowInteractor.getPhoneCodeId();
        verifyingCodeSubs = userInteractor.login(verifyCode, phoneNumber, phoneCodeId)
            .doOnTerminate(() -> {
                verifyingCodeSubs = null;
                if (view != null) {
                    view.hideAuthProgressDialog();
                }
            })
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(result -> {
                loginFlowInteractor.reset();
                if (view != null) {
                    if (!userInteractor.showTourForUser()) {
                        view.openChatScreen();
                    } else {
                        view.openTourScreen();
                    }
                }
            }, error -> {
                if (view != null) {
                    view.showError(error);
                }
            });
    }

    public void detachView() {
        this.view = null;
    }

    public void onShown() {
        if (view != null) {
            if (requestingSmsCodeSubs != null) {
                view.showRequestVerificationCodeProgressDialog();
            }
            if (verifyingCodeSubs != null) {
                view.showAuthorizationProgressDialog();
            }
        }
    }

    public void onHidden() {
        if (view != null) {
            view.showRequestVerificationCodeProgressDialog();
            view.showAuthorizationProgressDialog();
        }
    }

}
