package com.pairup.android;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class TestApp extends App {

    @Override protected void setUpServices() {
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean isValidDate(String date, String format) {

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);

        try {
            sdf.parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
