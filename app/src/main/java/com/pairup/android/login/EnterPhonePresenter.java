package com.pairup.android.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.pairup.android.UserInteractor;
import com.pairup.android.login.functional_area.models.Area;
import com.pairup.android.utils.Analytics;
import com.pairup.android.utils.Areas;
import com.pairup.android.utils.Rxs;
import com.pairup.android.utils.network.models.RetrofitException;

import java.util.List;

import rx.Subscription;

/**
 * Created by sergeyklymenko on 1/10/17.
 */

public class EnterPhonePresenter {

    private final UserInteractor      userInteractor;
    private final LoginFlowInteractor loginFlowInteractor;
    private final Analytics           analytics;


    @Nullable
    private EnterPhoneView view;

    private boolean      hasProvider;
    @Nullable
    private Subscription verifyNumberSubs;

    public EnterPhonePresenter(UserInteractor userInteractor,
                               LoginFlowInteractor loginFlowInteractor,
                               Analytics analytics) {
        this.userInteractor = userInteractor;
        this.loginFlowInteractor = loginFlowInteractor;
        this.analytics = analytics;
    }

    public void attachView(@NonNull EnterPhoneView view) {
        this.view = view;
    }

    public void onViewShown(ProgressDialog progressDialog) {
        if (verifyNumberSubs != null && progressDialog == null) {
            if (view != null) {
                view.showProgress();
            }
        }
    }

    private void unsubscribeSubs() {
        if (verifyNumberSubs != null) {
            verifyNumberSubs.unsubscribe();
            verifyNumberSubs = null;
        }
    }

    public String getAreaCode(@NonNull String areaCode) {
        return areaCode.startsWith("+") ? areaCode : ("+" + areaCode);
    }

    /**
     * @param areaCode should always starts with + in the head
     *                 cause getAreaCode() method was called before
     */
    public boolean isCorrectAreaCode(@NonNull String areaCode, @NonNull Context context) {
        String areaCodeWithoutPlus = areaCode.substring(1, areaCode.length());
        List<Area> areas = Areas.getAreas(context);
        for (Area area : areas) {
            if (areaCodeWithoutPlus.equals(area.getCode().trim()))
                return true;
        }
        return false;
    }

    public void onSubmitClick(String phone, String areaCode,
                              String providerName,
                              AccountVerificationType accountVerificationType,
                              Context context) {
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(areaCode)) {
            if (isCorrectAreaCode(areaCode, context)) {
                manageSubmit(areaCode, phone,
                    accountVerificationType, providerName);
            } else {
                if (view != null) {
                    view.registrationErrorFillAreaCode();
                }
            }
        } else {
            if (view != null) {
                view.registrationErrorFillPhone();
            }
        }
    }

    public void manageSubmit(@NonNull String areaCode,
                             @NonNull String phone,
                             @NonNull AccountVerificationType accountVerificationType,
                             @Nullable String providerName) {
        if (view != null) {
            view.hideKeyboard();
            view.showProgress();
        }

        final String formattedPhone;

        if ("+61".equals(areaCode) && '0' == phone.charAt(0)) {
            formattedPhone = phone.substring(1, phone.length());
        } else {
            formattedPhone = phone;
        }

        userInteractor.requestUserStatus(areaCode + formattedPhone)
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(result -> {
                boolean passed = false;
                hasProvider = result.userHasProvider();

                switch (accountVerificationType) {
                    case IS_REGISTERED:
                        passed = result.isPhoneRegistered();
                        break;
                    case IS_NOT_REGISTERED:
                        passed = !result.isPhoneRegistered();
                        break;
                    case HAS_PROVIDER:
                        if (providerName.equals(result.getProvider())) {
                            passed = hasProvider;
                        } else {
                            analytics.logWrongProviderChoosen();
                        }
                        break;
                    default:
                        break;
                }

                if (passed) {
                    requestAuthorizationCode(areaCode, formattedPhone);
                } else {
                    if (view != null) {
                        view.showErrorDialog();
                    }
                }
            }, error -> {
                if (view != null) {
                    view.handleError();
                }
            });
    }

    private void requestAuthorizationCode(@NonNull String areaCode, @NonNull String phone) {
        if (verifyNumberSubs != null) return;

        verifyNumberSubs = userInteractor.requestCode(areaCode + phone)
            .doOnTerminate(() -> {
                verifyNumberSubs = null;
                if (view != null) {
                    view.hideProgress();
                }
            })
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(result -> {
                loginFlowInteractor.setPhoneCodeId(result.getPhoneCodeId());
                loginFlowInteractor.setPhoneRegistered(result.isPhoneRegistered());
                loginFlowInteractor.setPhoneNumber(areaCode + phone);
                loginFlowInteractor.setHasProvider(hasProvider);

                if (view != null) {
                    view.openVerificationScreen();
                }
            }, error -> {
                if (error instanceof RetrofitException) {
                    RetrofitException retrofitError = (RetrofitException) error;
                    if (retrofitError.getKind() == RetrofitException.Kind.NETWORK) {
                        if (view != null) {
                            view.showDialogNetworkProblem();
                        }
                    } else {
                        if (view != null) {
                            view.showPhoneError();
                        }
                    }
                }
            });
    }

    public boolean hasProvider() {
        return hasProvider;
    }

    public void onCancelVerifyingProcess() {
        unsubscribeSubs();
    }

    public void detachView() {
        this.view = null;
    }
}
