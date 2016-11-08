package com.pairup.android.subscription;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.pairup.android.Navigator;
import com.pairup.android.R;

import javax.inject.Inject;

/**
 * Created by almaziskhakov on 04/11/2016.
 */

public class SubscriptionPresenter implements BillingProcessor.IBillingHandler {

    public static final  String ONE_MONTH_TEST_SUBSCRIPTION_ID = "one_month_test_subscription";
    public static final  String ONE_MONTH_SUBSCRIPTION_ID      = "one_month_subsription";
    public static final  String ONE_YEAR_SUBSCRIPTION_ID       = "one_year_subscription";
    private static final String UNSUBSCRIBE_LINK               = "https://play.google.com/store/account/subscriptions";
    private static final String PURCHASE_KEY                   = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAorESPZk0zw3hhu3kFGoGm1wsJJX/TJWOB/+q9LQ+VpN2TVuyzouVaYSxOSaHXg3/s1t4tUni7Ih3EVwR4//dbTH7ob3JdDoRzlWsgJaHeytH8qW6hPCdRX/cHLT0PbldwryUh92/yjBeel4Lo7McirS97MYElfsSQ52bEo8GOhG8SPYTHruh4WNp/LD/NO042AZUfi6+9fITzgNe2PeUKvGaFB9CrPpdbylExGhXhjjUhodZEjoUUtvCFG82lkvQHjnrUOs1PdHIhOk2IVVjpLHkX++9188ASEOflNNfnQIbRprjTuKFZG9NX/DTunzJNnH183fvyVQCX/r+ciFkAQIDAQAB";

    private SubscriptionView view;
    private FragmentActivity activity;
    private BillingProcessor bp;

    @Inject
    public SubscriptionPresenter() {}

    public void attachView(@NonNull SubscriptionView view, @NonNull FragmentActivity activity) {
        this.view = view;
        this.activity = activity;
        boolean isAvailable = BillingProcessor.isIabServiceAvailable(activity);
        if (isAvailable) {
            bp = new BillingProcessor(activity, PURCHASE_KEY, this);
        } else {
            Toast.makeText(activity, "Google play services unavailable", Toast.LENGTH_SHORT).show();
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

    public String getSubsciptionStatus() {
        //TODO make adecvat getting
        String status = "Your subscription expires in ";
//        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//        Date purchaseDate = bp.getSubscriptionTransactionDetails(ONE_MONTH_SUBSCRIPTION_ID).purchaseTime;
//        purchaseDate.setMonth(purchaseDate.getMonth()+1);
//        status+=df.format(purchaseDate);
        return status;
    }

    public void subscribe() {
        bp.subscribe(activity, ONE_MONTH_TEST_SUBSCRIPTION_ID);
    }

    public void unsubscribe(@NonNull FragmentActivity activity) {
        Navigator.openUrl(activity, UNSUBSCRIBE_LINK);
    }

    public void showSubscriptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setNeutralButton(R.string.subscribe, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                subscribe();
            }
        });
        builder.setTitle(R.string.subscription);
        builder.setMessage(R.string.subscription_dialog_message);
        builder.create().show();
    }

    @Override public void onProductPurchased(String productId, TransactionDetails details) {
        view.onProductPurchased(productId, details);
    }

    @Override public void onPurchaseHistoryRestored() {}

    @Override public void onBillingError(int errorCode, Throwable error) {}

    @Override public void onBillingInitialized() {}
}
