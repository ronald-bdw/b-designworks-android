package com.b_designworks.android.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.utils.di.Injector;
import com.b_designworks.android.utils.ui.AreYouSureDialog;
import com.b_designworks.android.utils.ui.UiInfo;

import javax.inject.Inject;

import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class SettingsScreen extends BaseActivity {

    @Inject UserInteractor userInteractor;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_settings).enableBackButton().setTitleRes(R.string.title_settings);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
    }

    @OnClick(R.id.sync) void onSyncClick(){
        Navigator.sync(context());
    }

    @OnClick(R.id.edit_profile) void onEditProfileClick(){
        Navigator.editProfile(context());
    }

    @OnClick(R.id.logout) void onLogoutClick() {
        AreYouSureDialog.show(context(), R.string.warning_you_will_lose_all_data, () -> {
            userInteractor.logout();
            Navigator.welcome(context());
        });
    }

}
