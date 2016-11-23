package com.pairup.android.chat.models;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by klim-mobile on 22.11.2016.
 */

public class SubscriptionsDetails {

    private static final String EXPIRED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String TYPE_TRIAL = "trial";

    @SerializedName("productId")
    private String planName;
    @SerializedName("purchaseTime")
    private long   purchaseDate;
    @SerializedName("autoRenewing")
    boolean isActive;

    public String getPlanName() {
        return TYPE_TRIAL;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPurchaseDate() {
        Date date = new Date(purchaseDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat(EXPIRED_DATE_FORMAT);
        return sdf.format(calendar.getTime());
    }

    public void setPurchaseDate(long purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
