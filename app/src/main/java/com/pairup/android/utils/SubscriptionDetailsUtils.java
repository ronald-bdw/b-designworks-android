package com.pairup.android.utils;

import java.util.Calendar;

/**
 * Created by klim-mobile on 27.11.2016.
 */

public class SubscriptionDetailsUtils {

    public static final String EXPIRED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static boolean isActive(boolean isRenewing, long purchaseDate) {
        return isRenewing ||
            (Times.now() < Times.addToDateTime(purchaseDate, Calendar.MONTH, 1).getTime());
    }

    public static String getExpiredDate(long purchaseDate) {
        return Times.parseDateToString(Times
            .addToDateTime(purchaseDate, Calendar.MONTH, 1), EXPIRED_DATE_FORMAT);
    }
}
