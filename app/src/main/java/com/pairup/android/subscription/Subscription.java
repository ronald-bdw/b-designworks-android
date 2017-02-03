package com.pairup.android.subscription;

/**
 * Created by almaziskhakov on 03/02/2017.
 */

public enum Subscription {

    THREE_MONTH_SUBSCRIPTION_ID("three_month_subscription_v1", 3),
    SIX_MONTH_SUBSCRIPTION_ID("six_month_subscription_v1", 6),
    ONE_YEAR_SUBSCRIPTION_ID("one_year_subscription_v1", 12);

    private String id;
    private int    months;

    Subscription(String id, int months) {
        this.id = id;
        this.months = months;
    }

    public String getPlanId() {
        return id;
    }

    public int getMonths() {
        return months;
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
