package com.pairup.android.profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pairup.android.BaseActivity;
import com.pairup.android.R;
import com.pairup.android.login.models.User;
import com.pairup.android.utils.ImageLoader;
import com.pairup.android.utils.Keyboard;
import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.network.ErrorUtils;
import com.pairup.android.utils.ui.SimpleDialog;
import com.pairup.android.utils.ui.SimpleLoadingDialog;
import com.pairup.android.utils.ui.TextViews;
import com.pairup.android.utils.ui.UiInfo;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.OnClick;
import gun0912.tedbottompicker.TedBottomPicker;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class EditProfileScreen extends BaseActivity implements EditProfileView {

    private static final String CROP_PROFILE_IMG_NAME = "profile_img.jpg";

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

    @BindColor(R.color.app_accent) int cropMainColor;
    @BindColor(R.color.settings_font) int cropDarkColor;

    @Inject EditProfilePresenter editProfilePresenter;
    @Inject ImageLoader          imageLoader;

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
        editProfilePresenter.attachView(this);
        editProfilePresenter.showUserInfo();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override public void showUserInfo(@NonNull User user) {
        showAvatar(user.getAvatar().getThumb());
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

    @Override public void incorrectImage() {
        SimpleDialog.withOkBtn(context(), getString(R.string.error_image_unacceptable));
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
                        .setOnImageSelectedListener(uri -> startCropImageActivity(uri))
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

    private void startCropImageActivity(@NonNull Uri imageLink) {
        UCrop.Options cropOptions = new UCrop.Options();
        cropOptions.setStatusBarColor(cropDarkColor);
        cropOptions.setToolbarColor(cropMainColor);
        cropOptions.setActiveWidgetColor(cropMainColor);
        cropOptions.setHideBottomControls(true);
        UCrop.of(imageLink, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), CROP_PROFILE_IMG_NAME)))
            .withOptions(cropOptions)
            .withAspectRatio(1,1)
            .start(this);
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

    @Override protected void onDestroy() {
        editProfilePresenter.detachView();
        super.onDestroy();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            String imagelink = imageLoader.getCorrectLink(UCrop.getOutput(data));
            if (imagelink == null) {
                incorrectImage();
            } else {
                editProfilePresenter.updateAvatar(imagelink);
            }
        }
    }
}
