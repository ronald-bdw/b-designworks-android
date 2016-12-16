package com.pairup.android.subscription;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;

import com.android.vending.billing.IInAppBillingService;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.gson.Gson;
import com.pairup.android.R;
import com.pairup.android.UserInteractor;
import com.pairup.android.chat.models.SubscriptionsDetails;
import com.pairup.android.utils.SubscriptionDetailsUtils;

import java.util.ArrayList;

import rx.schedulers.Schedulers;

/**
 * Created by almaziskhakov on 04/11/2016.
 */

public class SubscriptionPresenter implements BillingProcessor.IBillingHandler {

    private static final String THREE_MONTH_SUBSCRIPTION_ID = "three_month_subscription_v1";
    private static final String SIX_MONTH_SUBSCRIPTION_ID   = "six_month_subscription_v1";
    private static final String ONE_YEAR_SUBSCRIPTION_ID    = "one_year_subscription_v1";
    private static final String PURCHASE_KEY                = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8" +
        "AMIIBCgKCAQEAorESPZk0zw3hhu3kFGoGm1wsJJX/TJWOB/+q9LQ+VpN2TVuyzouVaYSxOSaHXg3/s1t4tUni" +
        "7Ih3EVwR4//dbTH7ob3JdDoRzlWsgJaHeytH8qW6hPCdRX/cHLT0PbldwryUh92/yjBeel4Lo7McirS97MYEl" +
        "fsSQ52bEo8GOhG8SPYTHruh4WNp/LD/NO042AZUfi6+9fITzgNe2PeUKvGaFB9CrPpdbylExGhXhjjUhodZEj" +
        "oUUtvCFG82lkvQHjnrUOs1PdHIhOk2IVVjpLHkX++9188ASEOflNNfnQIbRprjTuKFZG9NX/DTunzJNnH183f" +
        "vyVQCX/r+ciFkAQIDAQAB";

    private SubscriptionView     view;
    private FragmentActivity     activity;
    private BillingProcessor     bp;
    private SubscriptionsDetails subscriptionsDetails;
    private Gson                 gson;
    private UserInteractor       userInteractor;

    private IInAppBillingService mBillingService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBillingService = IInAppBillingService.Stub.asInterface(service);
            receiveSubscriptionDetails();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBillingService = null;
        }
    };

    public SubscriptionPresenter(Gson gson, UserInteractor userInteractor) {
        this.gson = gson;
        this.userInteractor = userInteractor;
    }

    public void attachView(@NonNull SubscriptionView view, @NonNull FragmentActivity activity) {
        this.view = view;
        this.activity = activity;
        boolean isAvailable = BillingProcessor.isIabServiceAvailable(activity);
        if (isAvailable) {
            bp = new BillingProcessor(activity, PURCHASE_KEY, this);
        } else {
            // TODO device what to when user has no google play services
        }
        initPayments();
    }

    public void detachView() {
        view = null;
        activity = null;
        if (bp != null) {
            bp.release();
        }
    }

    public BillingProcessor getBillingProcessor() {
        return bp;
    }

    public boolean isSubscribed() {
        return bp.isSubscribed(THREE_MONTH_SUBSCRIPTION_ID) ||
            bp.isSubscribed(SIX_MONTH_SUBSCRIPTION_ID) ||
            bp.isSubscribed(ONE_YEAR_SUBSCRIPTION_ID);
    }

    @StringRes public int getSubscriptionStatusText() {
        return isSubscribed() ? R.string.subscribed_status : R.string.subscription_request;
    }

    public void subscribe(int subscription) {
        switch (subscription) {
            case 0:
                bp.subscribe(activity, THREE_MONTH_SUBSCRIPTION_ID);
                break;
            case 1:
                bp.subscribe(activity, SIX_MONTH_SUBSCRIPTION_ID);
                break;
            case 2:
                bp.subscribe(activity, ONE_YEAR_SUBSCRIPTION_ID);
                break;
            default:
        }
    }

    public void showSubscriptionDialog() {
        view.showSubscriptionDialog();
    }

    private void initPayments() {
        Intent serviceIntent =
            new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");

        if (!activity.getPackageManager().queryIntentServices(serviceIntent, 0).isEmpty()) {
            activity.bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    public void receiveSubscriptionDetails() {
        ArrayList<String> skuList = new ArrayList<>();
        skuList.add(THREE_MONTH_SUBSCRIPTION_ID);
        skuList.add(SIX_MONTH_SUBSCRIPTION_ID);
        skuList.add(ONE_YEAR_SUBSCRIPTION_ID);
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

        try {
            Bundle skuDetails = mBillingService.getPurchases(3, activity.getPackageName(),
                "subs", null);

            int response = skuDetails.getInt("RESPONSE_CODE");
            if (response == 0) {
                ArrayList<String> purchaseDataList =
                    skuDetails.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                if (purchaseDataList != null && purchaseDataList.size() > 0) {
                    subscriptionsDetails = getSubscribeDataFromString(purchaseDataList.get(0));
                    userInteractor.sendInAppStatus(subscriptionsDetails.getPlanName(),
                        SubscriptionDetailsUtils
                            .getExpiredDate(subscriptionsDetails.getPurchaseDate()),
                        SubscriptionDetailsUtils.isActive(subscriptionsDetails.isRenewing(),
                            subscriptionsDetails.getPurchaseDate()))
                        .subscribeOn(Schedulers.io())
                        .subscribe(result -> { }, ignoreError -> { });
                } else if (userInteractor.getUser().getProvider() == null) {
                    userInteractor.sendInAppStatusExpired()
                        .subscribeOn(Schedulers.io())
                        .subscribe(result -> { }, ignoreError -> { });
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private SubscriptionsDetails getSubscribeDataFromString(@NonNull String purchaseData) {
        return gson.fromJson(purchaseData, SubscriptionsDetails.class);
    }

    @Override public void onProductPurchased(String productId, TransactionDetails details) {
        view.onProductPurchased(productId, details);
    }

    @Override public void onPurchaseHistoryRestored() {
    }

    @Override public void onBillingError(int errorCode, Throwable error) {
    }

    @Override public void onBillingInitialized() {
    }
}