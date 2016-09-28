package com.b_designworks.android.utils.di;

import com.b_designworks.android.InitialScreen;
import com.b_designworks.android.chat.ChatScreen;
import com.b_designworks.android.chat.ChatSidePanelFragment;
import com.b_designworks.android.login.RegistrationScreen;
import com.b_designworks.android.login.VerifyScreen;
import com.b_designworks.android.profile.EditProfileScreen;
import com.b_designworks.android.profile.ProfileScreen;
import com.b_designworks.android.settings.SettingsScreen;
import com.b_designworks.android.sync.FitbitScreen;
import com.b_designworks.android.sync.GoogleFitScreen;
import com.b_designworks.android.tour_app.TourScreen1Profile;
import com.b_designworks.android.tour_app.TourScreen2UploadAvatar;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Ilya Eremin on 9/19/16.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(GoogleFitScreen activity);
    void inject(InitialScreen activity);
    void inject(RegistrationScreen registrationScreen);
    void inject(VerifyScreen verifyScreen);
    void inject(ChatScreen chatScreen);
    void inject(ChatSidePanelFragment chatSidePanelFragment);
    void inject(EditProfileScreen editProfileScreen);
    void inject(ProfileScreen profileScreen);
    void inject(SettingsScreen settingsScreen);
    void inject(FitbitScreen fitbitScreen);
    void inject(TourScreen1Profile tourScreen1Profile);
    void inject(TourScreen2UploadAvatar tourScreen2UploadAvatar);
}
