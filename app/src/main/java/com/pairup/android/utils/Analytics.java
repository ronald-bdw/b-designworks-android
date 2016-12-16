package com.pairup.android.utils;

import com.flurry.android.FlurryAgent;
import com.pairup.android.BuildConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Klymenko on 12.12.2016.
 */

public class Analytics {

    public static final String EVENT_OPEN_CHAT_SCREEN            = "open_chat_screen";
    public static final String EVENT_OPEN_WELCOME_SCREEN         = "open_welcome_screen";
    public static final String EVENT_OPEN_VERIFY_SCREEN          = "open_verify_screen";
    public static final String EVENT_OPEN_SELECT_PROVIDER_SCREEN = "open_select_provider_screen";
    public static final String EVENT_OPEN_REGISTRATION_SCREEN    = "open_registration_screen";
    public static final String EVENT_OPEN_ENTER_CODE_SCREEN      = "open_enter_code_screen";
    public static final String EVENT_OPEN_EDIT_PROFILE_SCREEN    = "open_edit_profile_screen";
    public static final String EVENT_OPEN_PROFILE_SCREEN         = "open_profile_screen";
    public static final String EVENT_OPEN_SETTINGS_SCREEN        = "open_settings_screen";
    public static final String EVENT_OPEN_SYNC_SCREEN            = "open_sync_screen";
    public static final String EVENT_OPEN_TRIAL_SCREEN           = "open_trial_screen";
    public static final String EVENT_OPEN_TOUR_PROFILE_SCREEN    = "open_tour_profile_screen";
    public static final String EVENT_OPEN_TOUR_AVATAR_SCREEN     = "open_tour_avatar_screen";
    public static final String EVENT_MESSAGE_RESPONSE            = "message_response";
    public static final String EVENT_WRONG_FLOW                  = "wrong_flow";

    public static final String PARAM_MESSAGE_TIME_RESPONSE = "message_time_response";

    public static void logScreenOpened(String screenName) {
        logEvent(screenName);
    }

    public static void logUserResponseSpeed() {
        Date lastCoachDate = ChatUtil.getLastMessageFromCoachDate();
        if (lastCoachDate != null) {
            Map<String, String> param = new HashMap<>();
            param.put(PARAM_MESSAGE_TIME_RESPONSE,
                Long.toString(Times.timePassedFromInSeconds(lastCoachDate)));
            logEvent(Analytics.EVENT_MESSAGE_RESPONSE, param);
        }
    }

    public static void logEvent(String event) {
        if (BuildConfig.FLAVOR.equals("production")) {
            FlurryAgent.logEvent(event);
        }
    }

    public static void logEvent(String event, Map<String, String> param) {
        if (BuildConfig.FLAVOR.equals("production")) {
            FlurryAgent.logEvent(event, param);
        }
    }
}
