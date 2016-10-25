package com.b_designworks.android.subscription;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.ui.UiInfo;

import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 10/21/16.
 */

public class SubscriptionScreen extends BaseActivity implements BillingProcessor.IBillingHandler {

    private BillingProcessor bp;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_subscription);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        boolean isAvailable = BillingProcessor.isIabServiceAvailable(context());
        if (isAvailable) {
            bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxBZknImWwlwsep/YFXFaO1q2ZkpWTadNeGYwDylJfP463ar2ipCTtDz2CnQVcO+sa7o/7+kIKRXlNim9vXu98mYNvKdNVHjhCk44Bopbpxuez8nOhubVZvNH6/L461qJz393rnH3ZCmnp+teFrAjnKQPnQ5dkuZ1S8UkPtQtN82V4+M701JPhcz+DKnn1Y5eo9RG6X+ak+4FdUqEzIOBv/akcf6qRtTvGz0w1TiFJNjScxhh8jkb/AI4mzArvvC7Y23xaXAIPUaQ+Y13Z+y2jkMEyCJyRWbleNwn2qesCb5wBb5+suVM1P3RVRtYMuLEwpXKQFGopgE+QLKhjiHAawIDAQAB", this);
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
        bp.subscribe(this, "super_pear_up_subsription_1_month");
    }

    @OnClick(R.id.unsubscribe) void onUnsubscribeClick() {
        bp.consumePurchase("super_pear_up_subsription_1_month");
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
