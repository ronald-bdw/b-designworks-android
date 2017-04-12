package com.pairup.android.subscription;

import android.support.annotation.Nullable;

/**
 * Created by almaziskhakov on 03/02/2017.
 */

public enum Subscription {

    THREE_MONTH_SUBSCRIPTION_ID("three_month_subscription_v4", "habit_starter", 3),
    SIX_MONTH_SUBSCRIPTION_ID("six_month_subscription_v4", "habit_stabilizer", 6),
    ONE_YEAR_SUBSCRIPTION_ID("one_year_subscription_v4", "habit_master", 12),

    THREE_MONTH_SUBSCRIPTION_WITHOUT_TRIAL_ID
        ("three_month_subscription_v3_without_trial", "habit_starter", 3),
    SIX_MONTH_SUBSCRIPTION_WITHOUT_TRIAL_ID
        ("six_month_subscription_v3_without_trial", "habit_stabilizer", 6),
    ONE_YEAR_SUBSCRIPTION_WITHOUT_TRIAL_ID
        ("one_year_subscription_v3_without_trial", "habit_master", 12),

    THREE_MONTH_SUBSCRIPTION_ID_V1("three_month_subscription_v1", "habit_starter", 3),
    SIX_MONTH_SUBSCRIPTION_ID_V1("six_month_subscription_v1", "habit_stabilizer", 6),
    ONE_YEAR_SUBSCRIPTION_ID_V1("one_year_subscription_v1", "habit_master", 12),

    THREE_MONTH_SUBSCRIPTION_ID_V2("three_month_subscription_v2", "habit_starter", 3),
    SIX_MONTH_SUBSCRIPTION_ID_V2("six_month_subscription_v2", "habit_stabilizer", 6),
    ONE_YEAR_SUBSCRIPTION_ID_V2("one_year_subscription_v2", "habit_master", 12),

    THREE_MONTH_SUBSCRIPTION_ID_V3("three_month_subscription_v3", "habit_starter", 3),
    SIX_MONTH_SUBSCRIPTION_ID_V3("six_month_subscription_v3", "habit_stabilizer", 6),
    ONE_YEAR_SUBSCRIPTION_ID_V3("one_year_subscription_v3", "habit_master", 12);

    private String planId;
    private String planName;
    private int    months;

    Subscription(String planId, String planName, int months) {
        this.planId = planId;
        this.planName = planName;
        this.months = months;
    }

    public String getPlanId() {
        return planId;
    }

    public String getPlanName() {
        return planName;
    }

    public int getMonths() {
        return months;
    }

    @Override public String toString() {
        return getPlanId();
    }

    @Nullable public static Subscription getEnum(String planId) {
        for (Subscription v : values())
            if (v.getPlanId().equalsIgnoreCase(planId)) return v;
        return null;
    }
}
