package com.pairup.android.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.TextView;

import com.pairup.android.BaseActivity;
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
    @Bind(R.id.fourth_line_tv)       TextView     uiFourthLineText;
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
        if(userInteractor.isSdkSupportsNotifications()) {
            if (userInteractor.isNotificationsEnabled(this)) {
                uiStatus.setText(R.string.notification_status_on);
                uiHint.setText(R.string.notification_1st_line_on);
                uiFourthLineText.setText(R.string.notifications_4th_line_on);
            } else {
                uiStatus.setText(R.string.notification_status_off);
                uiHint.setText(R.string.notification_1st_line_off);
                uiFourthLineText.setText(R.string.notifications_4th_line_off);
            }
            uiNotificationsToggle.setChecked(!userInteractor.isNotificationsEnabled(this));
        } else {
            uiStatus.setVisibility(View.GONE);
            uiFourthLineText.setText(R.string.notifications_4th_line);
            uiHint.setText(R.string.notification_1st_line);
        }
    }
}
