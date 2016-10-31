package com.pairup.android.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class AndroidUtils {
    private static float density = 1;
    static DisplayMetrics displayMetrics;

    public static int dp(float value) {
        return (int) Math.ceil(density * value);
    }

    public static void initialize(Context context) {
        displayMetrics = context.getResources().getDisplayMetrics();
        density = displayMetrics.density;
    }

}
