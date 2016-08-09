package com.b_designworks.android;

import android.support.annotation.NonNull;

import com.b_designworks.android.utils.UiInfo;

/**
 * Created by Ilya Eremin on 09.08.2016.
 */
public class AboutUsScreen extends BaseActivity {
    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_about_us).setTitleRes(R.string.title_about_us).enableBackButton();
    }
}
