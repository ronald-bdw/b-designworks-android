package com.pairup.android.chat.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Klymenko on 22.11.2016.
 */

public class SubscriptionsDetails {

    private static final String TYPE_TRIAL = "trial";

    @SerializedName("productId") private    String  planName;
    @SerializedName("purchaseTime") private long    purchaseDate;
    @SerializedName("autoRenewing") private boolean isRenewing;

    public String getPlanName() {
        //TODO change it when all subscriptions will be added
        return TYPE_TRIAL;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
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