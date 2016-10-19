package com.b_designworks.android.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.widget.ImageView;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.utils.di.Injector;
import com.b_designworks.android.utils.ImageLoader;
import com.b_designworks.android.utils.ui.AreYouSureDialog;
import com.b_designworks.android.utils.ui.UiInfo;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class SettingsScreen extends BaseActivity {

    @Inject UserInteractor userInteractor;

    @Bind(R.id.avatar)                      ImageView    uiAvatar;
    @Bind(R.id.notifications_switch_compat) SwitchCompat uiNotificationsSwitchCompat;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_settings).enableBackButton().setTitleRes(R.string.title_settings);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
        showNotificationsSwitchCompatIsChecked();
    }

    @Override protected void onResume() {
        super.onResume();
        ImageLoader.load(context(), uiAvatar, userInteractor.getUser().getAvatar().getOriginal());
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

    @OnCheckedChanged(R.id.notifications_switch_compat)
    void onNotificationsSwitchCompatCheckedChanged(boolean isChecked) {
        userInteractor.setNotificationsEnabled(isChecked);
    }

    private void showNotificationsSwitchCompatIsChecked() {
        uiNotificationsSwitchCompat.setChecked(userInteractor.isNotificationsEnabled());
    }

}
