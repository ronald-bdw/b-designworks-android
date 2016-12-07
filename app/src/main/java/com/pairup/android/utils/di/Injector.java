package com.pairup.android.utils.di;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.pairup.android.App;
import com.pairup.android.InitialScreen;
import com.pairup.android.chat.ChatScreen;
import com.pairup.android.chat.ChatSidePanelFragment;
import com.pairup.android.login.EnterPhoneScreen;
import com.pairup.android.login.RegistrationScreen;
import com.pairup.android.login.VerifyScreen;
import com.pairup.android.profile.EditProfileScreen;
import com.pairup.android.profile.ProfileScreen;
import com.pairup.android.settings.PushNotificationsSettingsScreen;
import com.pairup.android.settings.SettingsScreen;
import com.pairup.android.sync.SyncScreen;
import com.pairup.android.tour_app.TourScreenProfile;
import com.pairup.android.tour_app.TourScreenUploadAvatar;

/**
 * Created by Ilya Eremin on 9/19/16.
 */
public class Injector {

    public static void inject(@NonNull App app) {
        app.getAppComponent().inject(app);
    }

    public static void inject(@NonNull InitialScreen initialScreen) {
        getAppComponent(initialScreen).inject(initialScreen);
    }

    private static AppComponent getAppComponent(@NonNull FragmentActivity initialScreen) {
        return ((App) initialScreen.getApplicationContext()).getAppComponent();
    }

    public static void inject(RegistrationScreen registrationScreen) {
        getAppComponent(registrationScreen).inject(registrationScreen);
    }

    public static void inject(VerifyScreen verifyScreen) {
        getAppComponent(verifyScreen).inject(verifyScreen);
    }

    public static void inject(ChatScreen chatScreen) {
        getAppComponent(chatScreen).inject(chatScreen);
    }

    public static void inject(ChatSidePanelFragment chatSidePanelFragment) {
        getAppComponent(chatSidePanelFragment.getActivity()).inject(chatSidePanelFragment);
    }

    public static void inject(EditProfileScreen editProfileScreen) {
        getAppComponent(editProfileScreen).inject(editProfileScreen);
    }

    public static void inject(ProfileScreen profileScreen) {
        getAppComponent(profileScreen).inject(profileScreen);
    }

    public static void inject(SettingsScreen settingsScreen) {
        getAppComponent(settingsScreen).inject(settingsScreen);
    }

    public static void inject(TourScreenProfile tourScreenProfile) {
        getAppComponent(tourScreenProfile).inject(tourScreenProfile);
    }

    public static void inject(TourScreenUploadAvatar tourScreenUploadAvatar) {
        getAppComponent(tourScreenUploadAvatar).inject(tourScreenUploadAvatar);
    }

    public static void inject(SyncScreen syncScreen) {
        getAppComponent(syncScreen).inject(syncScreen);
    }

    public static void inject(PushNotificationsSettingsScreen pushNotificationsSettingsScreen) {
        getAppComponent(pushNotificationsSettingsScreen).inject(pushNotificationsSettingsScreen);
    }

    public static void inject(@NonNull EnterPhoneScreen enterPhoneScreen) {
        ((App) enterPhoneScreen.getApplicationContext()).getAppComponent().inject(enterPhoneScreen);
    }
}
