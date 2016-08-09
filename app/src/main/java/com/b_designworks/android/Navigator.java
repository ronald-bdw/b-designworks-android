package com.b_designworks.android;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.b_designworks.android.chat.ChatScreen;
import com.b_designworks.android.profile.EditProfileScreen;
import com.b_designworks.android.profile.ProfileScreen;
import com.b_designworks.android.registration.RegistrationScreen;
import com.b_designworks.android.settings.PushNotificationsSettingsScreen;
import com.b_designworks.android.settings.SettingsScreen;
import com.b_designworks.android.sync.SyncScreen;
import com.b_designworks.android.trial.TrialScreen;
import com.b_designworks.android.verification.VerifyScreen;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class Navigator {

    public static void trialPage(@NonNull Context context) {
        context.startActivity(new Intent(context, TrialScreen.class));
    }

    public static void verification(@NonNull Context context) {
        context.startActivity(new Intent(context, VerifyScreen.class));
    }

    public static void chat(Context context) {
        context.startActivity(new Intent(context, ChatScreen.class));
    }

    public static void profile(Context context) {
        context.startActivity(new Intent(context, ProfileScreen.class));
    }


    public static void settings(Context context) {
        context.startActivity(new Intent(context, SettingsScreen.class));
    }

    public static void pushNotifications(@NonNull Context context) {
        context.startActivity(new Intent(context, PushNotificationsSettingsScreen.class));
    }

    public static void sync(Context context) {
        context.startActivity(new Intent(context, SyncScreen.class));
    }

    public static void editProfile(Context context) {
        context.startActivity(new Intent(context, EditProfileScreen.class));
    }

    public static void signUp(Context context) {
        context.startActivity(new Intent(context, RegistrationScreen.class));
    }

    public static void aboutUs(Context context) {
        context.startActivity(new Intent(context, AboutUsScreen.class));
    }
}