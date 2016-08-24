package com.b_designworks.android.settings;

import android.support.annotation.NonNull;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.ui.UiInfo;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class PushNotificationsSettingsScreen extends BaseActivity {

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_push_notifications_settings)
            .enableBackButton()
            .setTitleRes(R.string.title_push_notifications);
    }

}
