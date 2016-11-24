package com.pairup.android.subscription;

import com.anjlab.android.iab.v3.TransactionDetails;

/**
 * Created by almaziskhakov on 04/11/2016.
 */

public interface SubscriptionView {
    void onProductPurchased(String productId, TransactionDetails details);
    void showSubscriptionDialog();
}
