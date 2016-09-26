package com.b_designworks.android.profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.DI;
import com.b_designworks.android.R;
import com.b_designworks.android.login.models.User;
import com.b_designworks.android.utils.ImageLoader;
import com.b_designworks.android.utils.Keyboard;
import com.b_designworks.android.utils.network.ErrorUtils;
import com.b_designworks.android.utils.ui.SimpleDialog;
import com.b_designworks.android.utils.ui.SimpleLoadingDialog;
import com.b_designworks.android.utils.ui.TextViews;
import com.b_designworks.android.utils.ui.UiInfo;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import butterknife.OnClick;
import gun0912.tedbottompicker.TedBottomPicker;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class EditProfileScreen extends BaseActivity implements EditProfileView {

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_edit_profile)
                .enableBackButton()
                .setTitleRes(R.string.title_edit_profile)
                .setMenuRes(R.menu.edit_profile);
    }

    @Bind(R.id.avatar)                    ImageView uiAvatar;
    @Bind(R.id.avatar_uploading_progress) View      uiAvatarUploadingProgress;
    @Bind(R.id.current_full_name)         TextView  uiCurrentFullName;
    @Bind(R.id.current_email)             TextView  uiCurrentEmail;
    @Bind(R.id.first_name)                EditText  uiFirstName;
    @Bind(R.id.last_name)                 EditText  uiLastName;
    @Bind(R.id.email)                     EditText  uiEmail;

    private EditProfilePresenter editProfilePresenter;

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        editProfilePresenter = new EditProfilePresenter(this, DI.getInstance().getUserInteractor());
        editProfilePresenter.showUserInfo();
    }

    @Override public void showUserInfo(@NonNull User user) {
        showAvatar(user.getAvatar().getOriginal());
        uiCurrentFullName.setText(getString(R.string.edit_profile_name_surname_pattern, user.getFirstName(), user.getLastName()));
        uiCurrentEmail.setText(user.getEmail());
        uiFirstName.setText(user.getFirstName());
        uiLastName.setText(user.getLastName());
        uiEmail.setText(user.getEmail());
    }

    @Nullable private ProgressDialog progressDialog;

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            editProfilePresenter.updateUser();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override @NonNull public String getEmail() {
        return TextViews.textOf(uiEmail);
    }

    @Override @NonNull public String getLastName() {
        return TextViews.textOf(uiLastName);
    }

    @Override @NonNull public String getFirstName() {
        return TextViews.textOf(uiFirstName);
    }

    @Override public void hideKeyboard() {
        Keyboard.hide(this);
    }

    @Override public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override protected void onResume() {
        super.onResume();
        editProfilePresenter.onScreenShown();
    }

    @Override public void showProgressDialog() {
        hideProgress();
        progressDialog = SimpleLoadingDialog.show(context(), getString(R.string.loading_user_info), () -> {
            editProfilePresenter.cancelRequest();
        });
    }

    @Override public void profileHasBeenUpdated() {
        Toast.makeText(this, R.string.edit_profile_profile_updated, Toast.LENGTH_SHORT).show();
    }

    @Override public void showError(Throwable error) {
        ErrorUtils.handle(context(), error);
    }

    @Override public void showEmailError(@StringRes int errorResId) {
        uiEmail.setError(getString(errorResId));
    }

    @OnClick(R.id.change_avatar) void onChangeAvatarClick() {
        RxPermissions.getInstance(this)
            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe(granted -> {
                if (granted) {
                    TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(this)
                        .setOnImageSelectedListener(uri -> editProfilePresenter.updateAvatar(uri.getPath()))
                        .create();
                    tedBottomPicker.show(getSupportFragmentManager());
                } else {
                    Toast.makeText(this, R.string.edit_profile_error_access_storage, Toast.LENGTH_SHORT).show();
                }
            });
    }

    public void showAvatar(@Nullable String imageUrl) {
        ImageLoader.load(context(), uiAvatar, imageUrl);
    }

    @Override public void showAvatarUploadingProgress() {
        uiAvatarUploadingProgress.setVisibility(View.VISIBLE);
    }

    @Override public void showUploadAvatarError(@Nullable String avatarUrl) {
        uiAvatarUploadingProgress.setVisibility(View.GONE);
        SimpleDialog.show(context(), getString(R.string.error), getString(R.string.error_uploading_photo),
                getString(R.string.retry), () -> editProfilePresenter.updateAvatar(avatarUrl),
                getString(R.string.cancel), () -> editProfilePresenter.userCancelAvatarUploading());
    }

    @Override public void avatarSuccessfullyUploaded() {
        uiAvatarUploadingProgress.setVisibility(View.GONE);
    }

    @Override protected void onPause() {
        editProfilePresenter.onScreenHidden();
        super.onPause();
    }
}
