package com.pairup.android.chat.models;

import com.google.gson.annotations.SerializedName;
import com.pairup.android.subscription.SubscriptionPresenter;

/**
 * Created by Klymenko on 22.11.2016.
 */

public class SubscriptionsDetails {

    @SerializedName("productId") private    String  planId;
    @SerializedName("purchaseTime") private long    purchaseDate;
    @SerializedName("autoRenewing") private boolean isRenewing;

    public String getPlanId() {
        return planId;
    }

    public String getPlanName() {
        String planName = "";
        switch (planId) {
            case SubscriptionPresenter.THREE_MONTH_SUBSCRIPTION_ID:
                planName = "habit_starter";
                break;
            case SubscriptionPresenter.SIX_MONTH_SUBSCRIPTION_ID:
                planName = "habit_stabliser";
                break;
            case SubscriptionPresenter.ONE_YEAR_SUBSCRIPTION_ID:
                planName = "habit_master";
                break;
            default:
                break;
        }
        return planName;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public boolean isRenewing() {
        return isRenewing;
    }

    public long getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(long purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setRenewing(boolean renewing) {
        isRenewing = renewing;
    }
}