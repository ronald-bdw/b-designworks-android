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
    boolean isRenewing;

    public String getPlanName() {
        //TODO change it when all subscriptions will be added
        return TYPE_TRIAL;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getExpiredDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat(EXPIRED_DATE_FORMAT);
        return sdf.format(getExpiredDate());
    }

    public void setPurchaseDate(long purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public boolean isActive() {
        Date now = new Date();
        return isRenewing || (now.getTime() < getExpiredDate().getTime());
    }

    public void setRenewing(boolean renewing) {
        isRenewing = renewing;
    }

    private Date getExpiredDate() {
        Date date = new Date(purchaseDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }
}
