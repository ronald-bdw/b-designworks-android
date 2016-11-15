package com.pairup.android.utils;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;

import com.pairup.android.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class CropUtil {

    private static final String CROP_PROFILE_IMG_NAME       = "profile_img.jpg";

    public static void startCropImageActivity(@NonNull Activity activity,
                                       @NonNull Uri imageLink,
                                       @NonNull String filePath) {
        UCrop.Options cropOptions = new UCrop.Options();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cropOptions.setStatusBarColor(activity.getColor(R.color.settings_font));
            cropOptions.setToolbarColor(activity.getColor(R.color.app_accent));
            cropOptions.setActiveWidgetColor(activity.getColor(R.color.app_accent));
        } else {
            cropOptions.setStatusBarColor(activity.getResources().getColor(R.color.settings_font));
            cropOptions.setToolbarColor(activity.getResources().getColor(R.color.app_accent));
            cropOptions.setActiveWidgetColor(activity.getResources().getColor(R.color.app_accent));
        }
        cropOptions.setHideBottomControls(true);
        UCrop.of(imageLink, Uri.fromFile(new File(filePath, CROP_PROFILE_IMG_NAME)))
            .withOptions(cropOptions)
            .withAspectRatio(1, 1)
            .start(activity);
    }

}
