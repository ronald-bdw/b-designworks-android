package com.b_designworks.android.tour_app;

import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.ui.UiInfo;

import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 9/28/16.
 */

public class TourScreen4FitnessApps extends BaseActivity {

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_tour_4)
            .setTitleRes(R.string.title_tour_4)
            .enableBackButton()
            .setMenuRes(R.menu.menu_with_next_btn);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.next) {
            Navigator.tour5(context());
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fitbit) void onFitbitClick() {
        Navigator.fitbit(context());
    }

    @OnClick(R.id.google_fit) void onGoogleFitClick() {
        Navigator.googleFit(context());
    }

    @OnClick(R.id.skip) void onSkipClick() {
        Navigator.tour5(context());
    }
}
