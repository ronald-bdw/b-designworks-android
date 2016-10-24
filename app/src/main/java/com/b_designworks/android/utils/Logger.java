package com.b_designworks.android.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.BuildConfig;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Ilya Eremin on 10/13/16.
 */

public class Logger {

    private static final String TAG = "PEAR_UP_LOGGER";

    public static void e(@NonNull Throwable error) {
        Log.e(TAG, error.getMessage(), error);
    }

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void dToast(@NonNull Context context, @NonNull String msg) {
        if (BuildConfig.DEBUG && !BuildConfig.FLAVOR.equals("production")) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
