package com.pairup.android;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.pairup.android.chat.ChatScreen;
import com.pairup.android.login.AccountVerificationType;
import com.pairup.android.login.EnterPhoneScreen;
import com.pairup.android.login.RegistrationScreen;
import com.pairup.android.login.SelectProviderScreen;
import com.pairup.android.login.VerifyScreen;
import com.pairup.android.login.WelcomeScreen;
import com.pairup.android.login.functional_area.FunctionalToAreaCodeScreen;
import com.pairup.android.profile.EditProfileScreen;
import com.pairup.android.profile.ProfileScreen;
import com.pairup.android.settings.PushNotificationsSettingsScreen;
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

    public static void welcomeWithError(@NonNull Context context, boolean isPhoneRegistered) {
        Intent intent = new Intent(context, WelcomeScreen.class);
        intent.putExtra(WelcomeScreen.ARG_HAS_ERROR, true);
        intent.putExtra(WelcomeScreen.ARG_IS_PHONE_REGISTERED, isPhoneRegistered);
        context.startActivity(clearStack(intent));
    }

    public static void enterPhone(@NonNull Context context,
                                  @NonNull AccountVerificationType accountVerificationType) {
        Intent intent = new Intent(context, EnterPhoneScreen.class);
        intent.putExtra(EnterPhoneScreen.ARG_ACCOUNT_VERIFICATION_TYPE, accountVerificationType);
        context.startActivity(intent);
    }

    public static void enterPhone(@NonNull Context context,
                                  @NonNull AccountVerificationType accountVerificationType,
                                  @NonNull String providerName) {
        Intent intent = new Intent(context, EnterPhoneScreen.class);
        intent.putExtra(EnterPhoneScreen.ARG_ACCOUNT_VERIFICATION_TYPE, accountVerificationType);
        intent.putExtra(EnterPhoneScreen.ARG_PROVIDER_NAME, providerName);
        context.startActivity(intent);
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

    public static void chatWithGoogleFitIntegration(@NonNull Context context) {
        Intent intent = new Intent(context, ChatScreen.class);
        intent.putExtra(ChatScreen.ARG_NEED_GOOGLE_FIT_INTEGRATION, true);
        context.startActivity(clearStack(intent));
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
        context.startActivity(clearStack(
            RegistrationScreen.createIntent(context, phone, code, phoneCodeId)));
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
        enterPhoneScreen.startActivityForResult(
            new Intent(enterPhoneScreen, FunctionalToAreaCodeScreen.class), requestCode);
    }

    public static void share(@NonNull Context context, @NonNull String text) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        context.startActivity(Intent
            .createChooser(sharingIntent, context.getString(R.string.share_using)));
    }

    public static void notifications(@NonNull Context context) {
        context.startActivity(new Intent(context, PushNotificationsSettingsScreen.class));
    }

    public static void applicationSettings(@NonNull Context context) {
        try {
            Intent intent =
                new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:com.pairup.android"));
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent intent =
                new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            context.startActivity(intent);
        }
    }
}