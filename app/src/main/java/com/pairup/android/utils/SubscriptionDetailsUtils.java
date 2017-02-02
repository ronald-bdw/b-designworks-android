package com.pairup.android.utils;

import android.support.annotation.NonNull;

import com.pairup.android.chat.models.SubscriptionsDetails;
import com.pairup.android.subscription.SubscriptionPresenter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by klim-mobile on 27.11.2016.
 */

public class SubscriptionDetailsUtils {

    public static final String EXPIRED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static boolean isActive(@NonNull SubscriptionsDetails subscriptionsDetails) {
        return subscriptionsDetails.isRenewing() ||
            (Times.now() < getExpireDate(subscriptionsDetails).getTime());
    }

    public static String getFormattedExpiredDate(
        @NonNull SubscriptionsDetails subscriptionsDetails) {
        return Times.parseDateToString(getExpireDate(subscriptionsDetails), EXPIRED_DATE_FORMAT);
    }

    public static Date getExpireDate(@NonNull SubscriptionsDetails subscriptionsDetails) {
        int monthsCountToExpire = 0;
        switch (subscriptionsDetails.getPlanName()) {
            case SubscriptionPresenter.THREE_MONTH_SUBSCRIPTION_ID:
                monthsCountToExpire = 3;
                break;
            case SubscriptionPresenter.SIX_MONTH_SUBSCRIPTION_ID:
                monthsCountToExpire = 6;
                break;
            case SubscriptionPresenter.ONE_YEAR_SUBSCRIPTION_ID:
                monthsCountToExpire = 12;
                break;
            default:
                break;
        }
        return Times.addToDateTime(subscriptionsDetails.getPurchaseDate(),
            Calendar.MONTH, monthsCountToExpire);
    }
}
