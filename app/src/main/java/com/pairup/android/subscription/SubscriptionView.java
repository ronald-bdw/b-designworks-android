package com.pairup.android.subscription;

import com.anjlab.android.iab.v3.TransactionDetails;
import com.pairup.android.chat.models.SubscriptionsDetails;

/**
 * Created by almaziskhakov on 04/11/2016.
 */

public interface SubscriptionView {
    void onProductPurchased(String productId, TransactionDetails details);
    void showSubscriptionDialog();
    void sendSubscribeStatus(SubscriptionsDetails subscriptionsDetails);
}
