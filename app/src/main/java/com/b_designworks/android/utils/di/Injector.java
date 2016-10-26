package com.b_designworks.android.utils.di;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.b_designworks.android.App;
import com.b_designworks.android.InitialScreen;
import com.b_designworks.android.chat.ChatScreen;
import com.b_designworks.android.chat.ChatSidePanelFragment;
import com.b_designworks.android.chat.pushes.PushNotificationsHandlerService;
import com.b_designworks.android.login.EnterPhoneScreen;
import com.b_designworks.android.login.RegistrationScreen;
import com.b_designworks.android.login.SmsListener;
import com.b_designworks.android.login.VerifyScreen;
import com.b_designworks.android.profile.EditProfileScreen;
import com.b_designworks.android.profile.ProfileScreen;
import com.b_designworks.android.settings.SettingsScreen;
import com.b_designworks.android.sync.SyncScreen;
import com.b_designworks.android.tour_app.TourScreenProfile;
import com.b_designworks.android.tour_app.TourScreenUploadAvatar;

/**
 * Created by Ilya Eremin on 9/19/16.
 */
public class Injector {

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

    public static void inject(PushNotificationsHandlerService pushNotificationsHandlerService) {
        ((App) pushNotificationsHandlerService.getApplicationContext()).getAppComponent().inject(pushNotificationsHandlerService);
    }

    public static void inject(@NonNull EnterPhoneScreen enterPhoneScreen) {
        ((App) enterPhoneScreen.getApplicationContext()).getAppComponent().inject(enterPhoneScreen);
    }

    public static void inject(SmsListener smsListener, Context context) {
        ((App) context.getApplicationContext()).getAppComponent().inject(smsListener);
    }
}
