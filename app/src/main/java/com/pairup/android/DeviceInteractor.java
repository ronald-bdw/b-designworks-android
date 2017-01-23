package com.pairup.android;

import android.os.Build;

/**
 * Created by almaziskhakov on 22/11/2016.
 */

public class DeviceInteractor {
    public static boolean doesSdkSupportNotifications() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
}
