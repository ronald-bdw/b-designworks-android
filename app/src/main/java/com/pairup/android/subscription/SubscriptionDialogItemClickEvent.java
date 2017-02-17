package com.pairup.android.subscription;

/**
 * Created by almaziskhakov on 10/02/2017.
 */

public enum SubscriptionDialogItemClickEvent {

    STARTER(Subscription.THREE_MONTH_SUBSCRIPTION_ID),
    STABILIZER(Subscription.SIX_MONTH_SUBSCRIPTION_ID),
    MASTER(Subscription.ONE_YEAR_SUBSCRIPTION_ID);

    private Subscription subscription;

    SubscriptionDialogItemClickEvent(Subscription subscription){
        this.subscription = subscription;
    }

    public Subscription getSubscription() {
        return subscription;
    }
}
