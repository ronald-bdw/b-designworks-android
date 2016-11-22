package com.pairup.android.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.TextView;

import com.pairup.android.BaseActivity;
import com.pairup.android.DeviceInteractor;
import com.pairup.android.Navigator;
import com.pairup.android.R;
import com.pairup.android.UserInteractor;
import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.ui.UiInfo;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class PushNotificationsSettingsScreen extends BaseActivity {

    @Bind(R.id.status)               TextView     uiStatus;
    @Bind(R.id.hint)                 TextView     uiHint;
    @Bind(R.id.change_notifications) TextView     uiChangeNotifications;
    @Bind(R.id.notifications_toggle) SwitchCompat uiNotificationsToggle;

    @Inject UserInteractor userInteractor;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_push_notifications_settings)
            .enableBackButton()
            .setTitleRes(R.string.title_push_notifications);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
    }

    @Override protected void onResume() {
        super.onResume();
        customizeUi();
    }

    @OnClick(R.id.application_settings) void onClickApplicationSettings() {
        Navigator.applicationSettings(this);
    }

    private void customizeUi() {
        if (DeviceInteractor.isSdkSupportsNotifications()) {
            if (userInteractor.areNotificationsEnabled()) {
                uiStatus.setText(R.string.notification_status_enabled);
                uiHint.setText(R.string.notification_hint_off);
                uiChangeNotifications.setText(R.string.notifications_change_off);
                uiNotificationsToggle.setChecked(false);
            } else {
                uiStatus.setText(R.string.notification_status_disabled);
                uiHint.setText(R.string.notification_hint_on);
                uiChangeNotifications.setText(R.string.notifications_change_on);
                uiNotificationsToggle.setChecked(true);
            }
            userInteractor.sendNotificationsStatus(userInteractor.areNotificationsEnabled());
        } else {
            uiStatus.setVisibility(View.GONE);
            uiChangeNotifications.setText(R.string.notifications_change);
            uiHint.setText(R.string.notification_hint);
        }
    }
}
