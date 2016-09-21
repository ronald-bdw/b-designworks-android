package com.b_designworks.android.login;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.utils.Rxs;
import com.b_designworks.android.utils.Strings;

import rx.Subscription;

/**
 * Created by Ilya Eremin on 9/21/16.
 */
public class VerifyPresenter {

    private final UserInteractor userInteractor;

    @Nullable private VerifyView view;

    private boolean userRegistered;

    @Nullable private String phoneCodeId;
    @Nullable private String phoneNumber;

    public VerifyPresenter(UserInteractor userInteractor) {
        this.userInteractor = userInteractor;
    }

    @Nullable private Subscription requestingSmsCodeSubs;

    public void attachView(@NonNull VerifyView view) {
        this.view = view;
    }

    public void requestCode(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
                if (view != null) {
                    view.showWaitingForSmsProgress();
                }
                userRegistered = result.isPhoneRegistered();
                phoneCodeId = result.getPhoneCodeId();
            }, view::showError);
    }

    public void handleSmsCode(String verificadtionCode) {
        if (!Strings.isEmpty(verificadtionCode)) {
            if (userRegistered) {
                login(verificadtionCode);
            } else {
                view.openRegistrationScreen(verificadtionCode, phoneNumber, phoneCodeId);
            }
        } else {
            if (view != null) {
                view.showVerificationCodeError();
            }
        }
    }

    @Nullable private Subscription verifyingCodeSubs;

    private void login(@NonNull String verifyCode) {
        if (verifyingCodeSubs != null && !verifyingCodeSubs.isUnsubscribed() || verifyingCodeSubs != null)
            return;
        if (view != null) {
            view.showAuthorizationProgressDialog();
        }
        verifyingCodeSubs = userInteractor.verifyCode(verifyCode, phoneNumber, phoneCodeId)
            .doOnTerminate(() -> {
                verifyingCodeSubs = null;
                if (view != null) {
                    view.hideAuthProgressDialog();
                }
            })
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(result -> view.openChatScreen(), view::showError);
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
            if (phoneCodeId != null) {
                view.showWaitingForSmsProgress();
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
