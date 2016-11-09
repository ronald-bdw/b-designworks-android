package com.pairup.android.subscription;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.pairup.android.Navigator;
import com.pairup.android.R;
import com.pairup.android.utils.ui.SimpleDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import rx.functions.Action0;

/**
 * Created by almaziskhakov on 04/11/2016.
 */

public class SubscriptionPresenter implements BillingProcessor.IBillingHandler {

    private static final String ONE_MONTH_TEST_SUBSCRIPTION_ID = "one_month_test_subscription";
    private static final String PURCHASE_KEY                   = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAorESPZk0zw3hhu3kFGoGm1wsJJX/TJWOB/+q9LQ+VpN2TVuyzouVaYSxOSaHXg3/s1t4tUni7Ih3EVwR4//dbTH7ob3JdDoRzlWsgJaHeytH8qW6hPCdRX/cHLT0PbldwryUh92/yjBeel4Lo7McirS97MYElfsSQ52bEo8GOhG8SPYTHruh4WNp/LD/NO042AZUfi6+9fITzgNe2PeUKvGaFB9CrPpdbylExGhXhjjUhodZEjoUUtvCFG82lkvQHjnrUOs1PdHIhOk2IVVjpLHkX++9188ASEOflNNfnQIbRprjTuKFZG9NX/DTunzJNnH183fvyVQCX/r+ciFkAQIDAQAB";

    private SubscriptionView view;
    private FragmentActivity activity;
    private BillingProcessor bp;

    public void attachView(@NonNull SubscriptionView view, @NonNull FragmentActivity activity) {
        this.view = view;
        this.activity = activity;
        boolean isAvailable = BillingProcessor.isIabServiceAvailable(activity);
        if (isAvailable) {
            bp = new BillingProcessor(activity, PURCHASE_KEY, this);
        } else {
            // TODO device what to when user has no google play services
        }
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
        return bp.isSubscribed(ONE_MONTH_TEST_SUBSCRIPTION_ID);
    }

    public @StringRes int getSubscriptionStatusText() {
        return isSubscribed() ? R.string.subscribed_status : R.string.subscription_request;
    }

    public void subscribe() {
        bp.subscribe(activity, ONE_MONTH_TEST_SUBSCRIPTION_ID);
    }

    public void showSubscriptionDialog() {
        view.showSubscriptionDialog();
    }

    @Override public void onProductPurchased(String productId, TransactionDetails details) {
        view.onProductPurchased(productId, details);
    }

    @Override public void onPurchaseHistoryRestored() {}

    @Override public void onBillingError(int errorCode, Throwable error) {}

    @Override public void onBillingInitialized() {}
}