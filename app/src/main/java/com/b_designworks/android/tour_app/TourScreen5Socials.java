package com.b_designworks.android.tour_app;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.ui.UiInfo;

import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 9/28/16.
 */
public class TourScreen5Socials extends BaseActivity {

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_tour_socials)
            .enableBackButton()
            .setTitleRes(R.string.title_tour_4_page);
    }

    @OnClick(R.id.finish) void onFinishClick() {
        Navigator.chat(context());
    }

    @OnClick(R.id.facebook) void onFacebookClick() {
        Toast.makeText(this, "Open app facebook page", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.twitter) void onTwitterClick() {
        Toast.makeText(this, "Open app twitter page", Toast.LENGTH_SHORT).show();
    }
}
