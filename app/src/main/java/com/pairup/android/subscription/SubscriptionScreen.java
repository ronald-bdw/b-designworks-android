package com.pairup.android.subscription;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.pairup.android.BaseActivity;
import com.pairup.android.R;
import com.pairup.android.utils.ui.UiInfo;

import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 10/21/16.
 */

public class SubscriptionScreen extends BaseActivity implements BillingProcessor.IBillingHandler {

    public static final String ONE_MONTH_SUBSRIPTION_ID = "one_month_subsription";
    private static final String PURCHASE_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAorESPZk0zw3hhu3kFGoGm1wsJJX/TJWOB/+q9LQ+VpN2TVuyzouVaYSxOSaHXg3/s1t4tUni7Ih3EVwR4//dbTH7ob3JdDoRzlWsgJaHeytH8qW6hPCdRX/cHLT0PbldwryUh92/yjBeel4Lo7McirS97MYElfsSQ52bEo8GOhG8SPYTHruh4WNp/LD/NO042AZUfi6+9fITzgNe2PeUKvGaFB9CrPpdbylExGhXhjjUhodZEjoUUtvCFG82lkvQHjnrUOs1PdHIhOk2IVVjpLHkX++9188ASEOflNNfnQIbRprjTuKFZG9NX/DTunzJNnH183fvyVQCX/r+ciFkAQIDAQAB";

    private BillingProcessor bp;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_subscription);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        boolean isAvailable = BillingProcessor.isIabServiceAvailable(context());
        if (isAvailable) {
            bp = new BillingProcessor(this, PURCHASE_KEY, this);
        } else {
            Toast.makeText(this, "Google play services unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override public void onProductPurchased(String productId, TransactionDetails details) {
        Toast.makeText(this, "purchased: " + productId, Toast.LENGTH_SHORT).show();
    }

    @Override public void onPurchaseHistoryRestored() {
        Toast.makeText(this, "purchases restored", Toast.LENGTH_SHORT).show();
    }

    @Override public void onBillingError(int errorCode, Throwable error) {
        Toast.makeText(this, "Billing Error: #" + errorCode + " error: " + (error != null ? error.getMessage() : ""), Toast.LENGTH_SHORT).show();
    }

    @Override public void onBillingInitialized() {
        Toast.makeText(this, "Billing initialized", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.one_month_subscribe) void onOneMonthClick() {
        bp.subscribe(this, ONE_MONTH_SUBSRIPTION_ID);
    }

    @OnClick(R.id.unsubscribe) void onUnsubscribeClick() {
        bp.consumePurchase(ONE_MONTH_SUBSRIPTION_ID);
    }

    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }
}
