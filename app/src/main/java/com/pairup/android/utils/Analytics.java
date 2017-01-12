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

    public static final String EVENT_OPEN_CHAT_SCREEN            = "Conversation screen";
    public static final String EVENT_OPEN_WELCOME_SCREEN         = "Welcome screen";
    public static final String EVENT_OPEN_VERIFY_SCREEN          = "Verification screen";
    public static final String EVENT_OPEN_SELECT_PROVIDER_SCREEN = "Select provider screen";
    public static final String EVENT_OPEN_CHOOSE_AREA_SCREEN     = "Area code screen";
    public static final String EVENT_OPEN_REGISTRATION_SCREEN    = "Registration screen";
    public static final String EVENT_OPEN_ENTER_CODE_SCREEN      = "Enter code screen";
    public static final String EVENT_OPEN_EDIT_PROFILE_SCREEN    = "Profile Edit screen";
    public static final String EVENT_OPEN_PROFILE_SCREEN         = "Profile screen";
    public static final String EVENT_OPEN_SETTINGS_SCREEN        = "Settings screen";
    public static final String EVENT_OPEN_SYNC_SCREEN            = "Data sync screen";
    public static final String EVENT_OPEN_TRIAL_SCREEN           = "Trial Screens";
    public static final String EVENT_OPEN_TOUR_PROFILE_SCREEN    = "Tour app user info screen";
    public static final String EVENT_OPEN_ABPUT_US_SCREEN        = "About us screen";
    public static final String EVENT_OPEN_TOUR_AVATAR_SCREEN     = "Tour app avatar screen";
    public static final String EVENT_MESSAGE_RESPONSE            = "Message sent";
    public static final String EVENT_WRONG_FLOW                  = "Wrong flow";
    public static final String EVENT_WRONG_PROVIDER              = "Wrong provider selected";

    public static final String PARAM_MESSAGE_TIME_RESPONSE = "Response time";

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
