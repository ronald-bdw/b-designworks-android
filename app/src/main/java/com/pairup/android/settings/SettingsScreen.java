package com.pairup.android.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;

import com.pairup.android.BaseActivity;
import com.pairup.android.DeviceInteractor;
import com.pairup.android.Navigator;
import com.pairup.android.R;
import com.pairup.android.UserInteractor;
import com.pairup.android.utils.Analytics;
import com.pairup.android.utils.ImageLoader;
import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.ui.AreYouSureDialog;
import com.pairup.android.utils.ui.UiInfo;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class SettingsScreen extends BaseActivity {

    @Inject UserInteractor userInteractor;

    @Bind(R.id.avatar)               ImageView    uiAvatar;
    @Bind(R.id.notifications_toggle) SwitchCompat uiNotificationsToggle;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_settings)
            .enableBackButton()
            .setTitleRes(R.string.title_settings);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);

        Analytics.logScreenOpened(Analytics.EVENT_OPEN_SETTINGS_SCREEN);

        if (!DeviceInteractor.doesSdkSupportNotifications())
            uiNotificationsToggle.setVisibility(View.GONE);
    }

    @Override protected void onResume() {
        super.onResume();
        uiNotificationsToggle.setChecked(userInteractor.areNotificationsEnabled());
        ImageLoader.load(context(), uiAvatar, userInteractor.getUser().getAvatar().getThumb());
    }

    @OnClick(R.id.sync) void onSyncClick() {
        Navigator.sync(context());
    }

    @OnClick(R.id.edit_profile) void onEditProfileClick() {
        Navigator.editProfile(context());
    }

    @OnClick(R.id.logout) void onLogoutClick() {
        AreYouSureDialog.show(context(), R.string.warning_you_will_lose_all_data, () -> {
            userInteractor.logout();
            Navigator.welcome(context());
        });
    }

    @OnClick(R.id.notifications) void onNotificationClick() {
        Navigator.notifications(this);
    }

}
