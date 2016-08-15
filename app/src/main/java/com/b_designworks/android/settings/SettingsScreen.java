package com.b_designworks.android.settings;

import android.support.annotation.NonNull;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.ui.UiInfo;

import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class SettingsScreen extends BaseActivity {

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_settings).enableBackButton().setTitleRes(R.string.title_settings);
    }

    @OnClick(R.id.sync) void onSyncClick(){
        Navigator.sync(context());
    }

    @OnClick(R.id.edit_profile) void onEditProfileClick(){
        Navigator.editProfile(context());
    }

}
