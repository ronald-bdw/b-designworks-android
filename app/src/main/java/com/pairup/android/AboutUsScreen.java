package com.pairup.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pairup.android.utils.Analytics;
import com.pairup.android.utils.ui.UiInfo;

import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 09.08.2016.
 */
public class AboutUsScreen extends BaseActivity {

    private static final String WEBLINK = "http://pairup.im";

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_about_us)
            .setTitleRes(R.string.title_about_us)
            .enableBackButton();
    }

    @OnClick(R.id.website_link) void onClicKWeblink() {
        Navigator.openUrl(this, WEBLINK);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);

        Analytics.logScreenOpened(Analytics.EVENT_OPEN_ABPUT_US_SCREEN);
    }
}