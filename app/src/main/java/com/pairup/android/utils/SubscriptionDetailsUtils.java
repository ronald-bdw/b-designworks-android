package com.pairup.android.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by klim-mobile on 27.11.2016.
 */

public class SubscriptionDetailsUtils {

    private static final String EXPIRED_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static boolean isActive(boolean isRenewing, long purchaseDate) {
        Date now = new Date();
        return isRenewing || (now.getTime() < Times.addToDateTime(purchaseDate, Calendar.MONTH, 1).getTime());
    }

    public static String getExpiredDate(long purchaseDate) {
        return Times.parseDateToString(Times.addToDateTime(purchaseDate, Calendar.MONTH, 1), EXPIRED_DATE_FORMAT);
    }
}
