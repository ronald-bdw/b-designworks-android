package com.pairup.android.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class AndroidUtils {
    private static float sDensity = 1;

    private static DisplayMetrics sDisplayMetrics;

    public static int dp(float value) {
        return (int) Math.ceil(sDensity * value);
    }

    public static void initialize(Context context) {
        sDisplayMetrics = context.getResources().getDisplayMetrics();
        sDensity = sDisplayMetrics.density;
    }

    public static DisplayMetrics getDisplayMetrics() {
        return sDisplayMetrics;
    }
}
