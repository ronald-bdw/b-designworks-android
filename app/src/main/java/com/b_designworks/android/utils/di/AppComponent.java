package com.b_designworks.android.utils.di;

import com.b_designworks.android.InitialScreen;
import com.b_designworks.android.chat.ChatScreen;
import com.b_designworks.android.chat.ChatSidePanelFragment;
import com.b_designworks.android.chat.pushes.PushNotificationsHandlerService;
import com.b_designworks.android.login.EnterPhoneScreen;
import com.b_designworks.android.login.RegistrationScreen;
import com.b_designworks.android.login.VerifyScreen;
import com.b_designworks.android.profile.EditProfileScreen;
import com.b_designworks.android.profile.ProfileScreen;
import com.b_designworks.android.settings.SettingsScreen;
import com.b_designworks.android.sync.SyncScreen;
import com.b_designworks.android.tour_app.TourScreenProfile;
import com.b_designworks.android.tour_app.TourScreenUploadAvatar;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Ilya Eremin on 9/19/16.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
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
}
