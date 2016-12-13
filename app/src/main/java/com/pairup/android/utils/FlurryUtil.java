package com.pairup.android.utils;

import com.flurry.android.FlurryAgent;
import com.pairup.android.BuildConfig;

/**
 * Created by Klymenko on 12.12.2016.
 */

public class FlurryUtil {

    public static final String EVENT_OPEN_CHAT_SCREEN = "open_chat_screen";
    public static final String EVENT_OPEN_WELCOME_SCREEN = "open_welcome_screen";
    public static final String EVENT_OPEN_VERIFY_SCREEN = "open_verify_screen";
    public static final String EVENT_OPEN_SELECT_PROVIDER_SCREEN = "open_select_provider_screen";
    public static final String EVENT_OPEN_REGISTRATION_SCREEN = "open_registration_screen";
    public static final String EVENT_OPEN_ENTER_CODE_SCREEN = "open_enter_code_screen";
    public static final String EVENT_OPEN_EDIT_PROFILE_SCREEN = "open_edit_profile_screen";
    public static final String EVENT_OPEN_PROFILE_SCREEN = "open_profile_screen";
    public static final String EVENT_OPEN_SETTINGS_SCREEN = "open_settings_screen";
    public static final String EVENT_OPEN_SYNC_SCREEN = "open_sync_screen";
    public static final String EVENT_OPEN_TRIAL_SCREEN = "open_trial_screen";
    public static final String EVENT_OPEN_TOUR_PROFILE_SCREEN = "open_tour_profile_screen";
    public static final String EVENT_OPEN_TOUR_AVATAR_SCREEN = "open_tour_avatar_screen";

    public static void logEvent(String event) {
        if (BuildConfig.FLAVOR.equals("production")) {
            FlurryAgent.logEvent(event);
        }
    }
}
