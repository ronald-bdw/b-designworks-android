package com.pairup.android.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Klymenko on 23.11.2016.
 */

public class Times {

    public static Date addToDateTime(long startDate,
                                     @NonNull int addToDate,
                                     @NonNull int increaseAmount) {
        Date date = new Date(startDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(addToDate, increaseAmount);
        return calendar.getTime();
    }

    @SuppressLint("SimpleDateFormat")
    public static String parseDateToString(@NonNull Date date, @NonNull String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
