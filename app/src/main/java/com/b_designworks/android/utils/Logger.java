package com.b_designworks.android.utils;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Ilya Eremin on 10/13/16.
 */

public class Logger {

    private static final String TAG = "PEAR_UP_LOGGER";

    public static void e(@NonNull Throwable error) {
        Log.e(TAG, error.getMessage(), error);
    }
}
