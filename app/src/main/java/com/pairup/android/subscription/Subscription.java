package com.pairup.android.subscription;

/**
 * Created by almaziskhakov on 03/02/2017.
 */

public enum Subscription {

    THREE_MONTH_SUBSCRIPTION_ID("three_month_subscription_v1"),
    SIX_MONTH_SUBSCRIPTION_ID("six_month_subscription_v1"),
    ONE_YEAR_SUBSCRIPTION_ID("one_year_subscription_v1");

    private String id;

    Subscription(String id) {
        this.id = id;
    }

    public String getPlanId() {
        return id;
    }

    @Override public String toString() {
        return getPlanId();
    }

    public static Subscription getEnum(String planId) {
        for (Subscription v : values())
            if (v.getPlanId().equalsIgnoreCase(planId)) return v;
        throw new IllegalArgumentException();
    }
}
