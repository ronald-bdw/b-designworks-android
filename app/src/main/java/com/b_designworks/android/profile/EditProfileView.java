package com.b_designworks.android.profile;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.b_designworks.android.login.models.User;

/**
 * Created by Ilya Eremin on 9/12/16.
 */
public interface EditProfileView {
    void showUserInfo(@NonNull User user);
    void hideProgress();
    void showProgressDialog();
    void profileHasBeenUpdated();
    void showError(Throwable error);
    void showEmailError(@StringRes int errorResId);
    void showAvatar(@Nullable String uri);
    void showAvatarUploadingProgress();
    void showUploadAvatarError(String avatarUrl);
    void avatarSuccessfullyUploaded();

    @NonNull String getEmail();
    @NonNull String getLastName();
    @NonNull String getFirstName();
    void hideKeyboard();
    void incorrectImage();
}
