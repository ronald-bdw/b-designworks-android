package com.pairup.android.chat.models;

import com.google.gson.annotations.SerializedName;
import com.pairup.android.subscription.Subscription;

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
        return Subscription.getEnum(planId).getPlanName();
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