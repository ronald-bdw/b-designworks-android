package com.pairup.android.tour_app;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.pairup.android.BaseActivity;
import com.pairup.android.Navigator;
import com.pairup.android.R;
import com.pairup.android.UserInteractor;
import com.pairup.android.utils.CropUtil;
import com.pairup.android.utils.ImageLoader;
import com.pairup.android.utils.Rxs;
import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.ui.SimpleDialog;
import com.pairup.android.utils.ui.UiInfo;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import gun0912.tedbottompicker.TedBottomPicker;
import rx.Subscription;

/**
 * Created by Ilya Eremin on 9/28/16.
 */

public class TourScreenUploadAvatar extends BaseActivity {

    @Inject UserInteractor userInteractor;
    @Inject ImageLoader    imageLoader;
    @Inject File           filePath;

    @Bind(R.id.avatar)   ImageView uiAvatar;
    @Bind(R.id.progress) View      uiProgress;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_tour_upload_avatar)
            .enableBackButton()
            .setTitleRes(R.string.title_tour_2_page)
            .setMenuRes(R.menu.menu_with_next_btn);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
    }

    @OnClick(R.id.skip) void onSkipClick() {
        Navigator.chat(context());
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.next) {
            Navigator.chat(context());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.pick_photo, R.id.avatar}) void onChangeAvatarClick() {
        RxPermissions.getInstance(this)
            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .subscribe(granted -> {
                if (granted) {
                    TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(this)
                        .setOnImageSelectedListener(uri -> CropUtil.startCropImageActivity(this,
                            uri, filePath.getAbsolutePath()))
                        .create();
                    tedBottomPicker.show(getSupportFragmentManager());
                } else {
                    Toast.makeText(this, R.string.edit_profile_error_access_storage,
                        Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Nullable private Subscription uploadAvatarSubs;

    private void updateAvatar(String url) {
        if (uploadAvatarSubs != null) return;
        showAvatar(url);
        showAvatarUploadingProgress();

        uploadAvatarSubs = userInteractor.uploadAvatar(url)
            .compose(Rxs.doInBackgroundDeliverToUI())
            .subscribe(result -> {
                avatarSuccessfullyUploaded();
            }, error -> {
                showUploadAvatarError(url);
            });
    }

    @Override protected void onResume() {
        super.onResume();
        showAvatar(userInteractor.getUser().getAvatar().getThumb());
        uiProgress.setVisibility(View.GONE);
    }

    @Override protected void onStop() {
        if (uploadAvatarSubs != null && !uploadAvatarSubs.isUnsubscribed()) {
            uploadAvatarSubs.unsubscribe();
            uploadAvatarSubs = null;
        }
        super.onStop();
    }

    private void avatarSuccessfullyUploaded() {
        uiProgress.setVisibility(View.GONE);
        ImageLoader.load(context(), uiAvatar, userInteractor.getUser().getAvatar().getThumb());
    }

    private void showUploadAvatarError(String url) {
        uiProgress.setVisibility(View.GONE);
        SimpleDialog.show(context(), getString(R.string.error), getString(R.string.error_uploading_photo),
            getString(R.string.retry), () -> updateAvatar(url),
            getString(R.string.cancel), () -> showAvatar(userInteractor.getUser().getAvatar().getThumb()));
    }

    private void showAvatarUploadingProgress() {
        uiProgress.setVisibility(View.VISIBLE);
    }

    private void showAvatar(@Nullable String url) {
        if (url == null) {
            uiAvatar.setVisibility(View.GONE);
        } else {
            uiAvatar.setVisibility(View.VISIBLE);
            ImageLoader.load(context(), uiAvatar, url);
        }
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            String imagelink = imageLoader.getCorrectLink(UCrop.getOutput(data));
            if (imagelink == null) {
                SimpleDialog.withOkBtn(context(), getString(R.string.error_image_unacceptable));
            } else {
                updateAvatar(imagelink);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}