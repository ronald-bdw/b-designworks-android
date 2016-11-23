package com.pairup.android.chat.models;

import com.google.gson.annotations.SerializedName;
import com.pairup.android.utils.Times;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Klymenko on 22.11.2016.
 */

public class SubscriptionsDetails {

    private static final String EXPIRED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String TYPE_TRIAL = "trial";

    @SerializedName("productId")
    private String planName;
    @SerializedName("purchaseTime")
    private long   purchaseDate;
    @SerializedName("autoRenewing")
    boolean isRenewing;

    public String getPlanName() {
        //TODO change it when all subscriptions will be added
        return TYPE_TRIAL;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getExpiredDate() {
        return Times.parseDateToString(Times.addToDateTime(purchaseDate, Calendar.MONTH, 1), EXPIRED_DATE_FORMAT);
    }

    public void setPurchaseDate(long purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public boolean isActive() {
        Date now = new Date();
        return isRenewing || (now.getTime() < Times.addToDateTime(purchaseDate, Calendar.MONTH, 1).getTime());
    }

    public void setRenewing(boolean renewing) {
        isRenewing = renewing;
    }
}
