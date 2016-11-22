package com.pairup.android.utils.di;

import com.pairup.android.App;
import com.pairup.android.InitialScreen;
import com.pairup.android.chat.ChatScreen;
import com.pairup.android.chat.ChatSidePanelFragment;
import com.pairup.android.chat.pushes.PushNotificationsHandlerService;
import com.pairup.android.login.EnterPhoneScreen;
import com.pairup.android.login.RegistrationScreen;
import com.pairup.android.login.VerifyScreen;
import com.pairup.android.profile.EditProfileScreen;
import com.pairup.android.profile.ProfileScreen;
import com.pairup.android.settings.PushNotificationsSettingsScreen;
import com.pairup.android.settings.SettingsScreen;
import com.pairup.android.subscription.SubscriptionScreen;
import com.pairup.android.sync.SyncScreen;
import com.pairup.android.tour_app.TourScreenProfile;
import com.pairup.android.tour_app.TourScreenUploadAvatar;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Ilya Eremin on 9/19/16.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(App app);
    void inject(InitialScreen activity);
    void inject(RegistrationScreen registrationScreen);
    void inject(VerifyScreen verifyScreen);
    void inject(ChatScreen chatScreen);
    void inject(ChatSidePanelFragment chatSidePanelFragment);
    void inject(EditProfileScreen editProfileScreen);
    void inject(ProfileScreen profileScreen);
    void inject(SettingsScreen settingsScreen);
    void inject(TourScreenProfile tourScreenProfile);
    void inject(TourScreenUploadAvatar tourScreenUploadAvatar);
    void inject(PushNotificationsHandlerService pushNotificationsHandlerService);
    void inject(EnterPhoneScreen enterPhoneScreen);
    void inject(SyncScreen syncScreen);
    void inject(SubscriptionScreen subscriptionScreen);
    void inject(PushNotificationsSettingsScreen pushNotificationsSettingsScreen);
}
