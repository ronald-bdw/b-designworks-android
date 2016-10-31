package com.pairup.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.pairup.android.R;
import com.pairup.android.chat.ChatScreen;
import com.pairup.android.login.EnterPhoneScreen;
import com.pairup.android.login.RegistrationScreen;
import com.pairup.android.login.SelectProviderScreen;
import com.pairup.android.login.VerifyScreen;
import com.pairup.android.login.WelcomeScreen;
import com.pairup.android.login.functional_area.FunctionalToAreaCodeScreen;
import com.pairup.android.profile.EditProfileScreen;
import com.pairup.android.profile.ProfileScreen;
import com.pairup.android.settings.SettingsScreen;
import com.pairup.android.sync.SyncScreen;
import com.pairup.android.tour_app.TourScreenProfile;
import com.pairup.android.tour_app.TourScreenUploadAvatar;
import com.pairup.android.trial.TrialScreen;

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

    public static void enterPhoneAndClearStack(@NonNull Context context) {
        context.startActivity(clearStack(new Intent(context, EnterPhoneScreen.class)));
    }

    public static void trialPage(@NonNull Context context) {
        context.startActivity(new Intent(context, TrialScreen.class));
    }

    public static void verification(@NonNull Context context) {
        context.startActivity(new Intent(context, VerifyScreen.class));
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

    public static void registration(@NonNull Context context, @NonNull String phone,
                                    @NonNull String code, @NonNull String phoneCodeId) {
        context.startActivity(RegistrationScreen.createIntent(context, phone, code, phoneCodeId));
    }

    public static void openUrl(@NonNull Context context, @NonNull String url) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public static void tour(Context context) {
        context.startActivity(clearStack(new Intent(context, TourScreenProfile.class)));
    }

    public static void tourUploadAvatar(@NonNull Context context) {
        context.startActivity(new Intent(context, TourScreenUploadAvatar.class));
    }

    public static void areaCode(@NonNull EnterPhoneScreen enterPhoneScreen, int requestCode) {
        enterPhoneScreen.startActivityForResult(new Intent(enterPhoneScreen, FunctionalToAreaCodeScreen.class), requestCode);
    }

    public static void share(@NonNull Context context, @NonNull String text) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(sharingIntent, context.getString(R.string.share_using)));
    }
}