package com.b_designworks.android.settings;

import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.DI;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.ImageLoader;
import com.b_designworks.android.utils.ui.AreYouSureDialog;
import com.b_designworks.android.utils.ui.UiInfo;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class SettingsScreen extends BaseActivity {

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_settings).enableBackButton().setTitleRes(R.string.title_settings);
    }

    @Bind(R.id.avatar) ImageView uiAvatar;

    @Override protected void onResume() {
        super.onResume();
        ImageLoader.load(context(), uiAvatar, DI.getInstance().getUserInteractor().getUser().getAvatar().getOriginal());
    }

    @OnClick(R.id.sync) void onSyncClick(){
        Navigator.sync(context());
    }

    @OnClick(R.id.edit_profile) void onEditProfileClick(){
        Navigator.editProfile(context());
    }

    @OnClick(R.id.logout) void onLogoutClick() {
        AreYouSureDialog.show(context(), R.string.warning_you_will_lose_all_data, () -> {
            DI.getInstance().getUserInteractor().logout();
            Navigator.welcome(context());
        });
    }

}
