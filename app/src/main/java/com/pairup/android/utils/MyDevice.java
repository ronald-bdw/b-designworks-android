package com.pairup.android.utils;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by Ilya Eremin on 22.08.2016.
 */
public class MyDevice {

    public static boolean isLocationEnabled(Context context) {
        final LocationManager manager = (LocationManager)
            context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static int getScreenWidthInPixels() {
        return AndroidUtils.getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeightInPixels() {
        return AndroidUtils.getDisplayMetrics().heightPixels;
    }
}
