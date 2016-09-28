package com.b_designworks.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.b_designworks.android.chat.ChatScreen;
import com.b_designworks.android.login.EnterPhoneScreen;
import com.b_designworks.android.login.RegistrationScreen;
import com.b_designworks.android.login.SelectProviderScreen;
import com.b_designworks.android.login.VerifyScreen;
import com.b_designworks.android.login.WelcomeScreen;
import com.b_designworks.android.profile.EditProfileScreen;
import com.b_designworks.android.profile.ProfileScreen;
import com.b_designworks.android.settings.PushNotificationsSettingsScreen;
import com.b_designworks.android.settings.SettingsScreen;
import com.b_designworks.android.sync.FitbitScreen;
import com.b_designworks.android.sync.GoogleFitScreen;
import com.b_designworks.android.sync.SyncScreen;
import com.b_designworks.android.trial.TrialScreen;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class Navigator {

    public static void welcome(@NonNull Context context) {
        context.startActivity(clearStack(new Intent(context, WelcomeScreen.class)));
    }

    public static void enterPhone(@NonNull Context context) {
        context.startActivity(new Intent(context, EnterPhoneScreen.class));
    }

    public static void trialPage(@NonNull Context context) {
        context.startActivity(new Intent(context, TrialScreen.class));
    }

    public static void verification(@NonNull Context context, String phone) {
        context.startActivity(VerifyScreen.createIntent(context, phone));
    }

    public static void chat(@NonNull Context context) {
        context.startActivity(clearStack(new Intent(context, ChatScreen.class)));
    }

    private static Intent clearStack(Intent intent) {
        return intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    public static void profile(@NonNull Context context) {
        context.startActivity(new Intent(context, ProfileScreen.class));
    }

    public static void settings(@NonNull Context context) {
        context.startActivity(new Intent(context, SettingsScreen.class));
    }

    public static void pushNotifications(@NonNull Context context) {
        context.startActivity(new Intent(context, PushNotificationsSettingsScreen.class));
    }

    public static void sync(@NonNull Context context) {
        context.startActivity(new Intent(context, SyncScreen.class));
    }

    public static void editProfile(@NonNull Context context) {
        context.startActivity(new Intent(context, EditProfileScreen.class));
    }

    public static void aboutUs(@NonNull Context context) {
        context.startActivity(new Intent(context, AboutUsScreen.class));
    }

    public static void selectProvider(@NonNull Context context) {
        context.startActivity(new Intent(context, SelectProviderScreen.class));
    }

    public static void registration(
        @NonNull Context context,
        @NonNull String code, @NonNull String phoneNumber, @NonNull String phoneCodeId) {
        context.startActivity(RegistrationScreen.createIntent(context, code, phoneNumber, phoneCodeId));
    }

    public static void registration(@NonNull Context context) {
        context.startActivity(new Intent(context, RegistrationScreen.class));
    }

    public static void verifyAndReturnCode(@NonNull BaseActivity activity,
                                           @NonNull String phone) {
        activity.startActivityForResult(VerifyScreen.createIntent(activity, phone), RegistrationScreen.RESULT_KEY_FOR_VERIFYING);
    }

    public static void googleFit(@NonNull BaseActivity activity) {
        activity.startActivity(new Intent(activity, GoogleFitScreen.class));
    }

    public static void fitbit(@NonNull Context context) {
        context.startActivity(new Intent(context, FitbitScreen.class));
    }

    public static void openUrl(@NonNull Context context, @NonNull String url) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public static void tour(Context context) {
        context.startActivity(clearStack(new Intent(context, TourScreen1Profile.class)));
    }

    public static void tour2(@NonNull Context context) {
        context.startActivity(new Intent(context, TourScreen2UploadAvatar.class));
    }

    public static void tour3(@NonNull Context context) {
        context.startActivity(new Intent(context, TourScreen3PushNotifications.class));
    }

    public static void tour4(@NonNull Context context) {
        context.startActivity(new Intent(context, TourScreen4FitnessApps.class));
    }
}