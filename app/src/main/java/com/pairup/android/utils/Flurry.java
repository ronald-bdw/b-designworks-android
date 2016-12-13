package com.pairup.android.utils;

import com.flurry.android.FlurryAgent;

/**
 * Created by Klymenko on 12.12.2016.
 */

public class Flurry {

    public static final String EVENT_OPEN_CHAT_SCREEN = "open_chat_screen";
    public static final String EVENT_OPEN_LOGIN_SCREEN = "open_login_screen";
    public static final String EVENT_OPEN_PROFILE_SCREEN = "open_profile_screen";
    public static final String EVENT_OPEN_SETTINGS_SCREEN = "open_settings_screen";
    public static final String EVENT_OPEN_SYNC_SCREEN = "open_sync_screen";
    public static final String EVENT_OPEN_TRIAL_SCREEN = "open_trial_screen";

    private static void logEvent(String event) {
        FlurryAgent.logEvent(event);
    }
}
