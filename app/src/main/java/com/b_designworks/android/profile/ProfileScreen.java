package com.b_designworks.android.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.DI;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.login.models.User;
import com.b_designworks.android.utils.ImageLoader;
import com.b_designworks.android.utils.ui.UiInfo;

import butterknife.Bind;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class ProfileScreen extends BaseActivity {

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_profile).enableBackButton().setMenuRes(R.menu.profile);
    }

    @Bind(R.id.avatar)            ImageView uiAvatar;
    @Bind(R.id.email)             TextView  uiEmail;
    @Bind(R.id.phone)             TextView  uiPhone;
    @Bind(R.id.current_full_name) TextView  uiCurrentFullName;
    @Bind(R.id.current_email)     TextView  uiCurrentEmail;

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        User user = DI.getInstance().getUserInteractor().getUser();
        ImageLoader.load(this, uiAvatar, user.getAvatar().getOriginal());
        uiCurrentFullName.setText(getString(R.string.edit_profile_name_surname_pattern, user.getFirstName(), user.getLastName()));
        uiEmail.setText(user.getEmail());
        uiCurrentEmail.setText(user.getEmail());
        uiPhone.setText(user.getPhoneNumber());
        setTitle(user.getFirstName());
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            Navigator.editProfile(context());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
