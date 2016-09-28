package com.b_designworks.android.tour_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.widget.TextView;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.login.models.User;
import com.b_designworks.android.utils.di.Injector;
import com.b_designworks.android.utils.ui.UiInfo;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 9/28/16.
 */

public class TourScreen1Profile extends BaseActivity {

    @Bind(R.id.first_name) TextView uiFirstName;
    @Bind(R.id.last_name)  TextView uiLastName;
    @Bind(R.id.email)      TextView uiEmail;

    @Inject UserInteractor userInteractor;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_tour_1)
            .setTitleRes(R.string.title_tour_1)
            .setMenuRes(R.menu.menu_with_next_btn);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
        User user = userInteractor.getUser();
        uiFirstName.setText(user.getFirstName());
        uiLastName.setText(user.getLastName());
        uiEmail.setText(user.getEmail());
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.next) {
            Navigator.tour2(context());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.next) void onSubmitClick() {
        // TODO update profile before redirect
        Navigator.tour2(context());
    }

    @OnClick(R.id.skip) void onSkipClick() {
        Navigator.tour2(context());
    }

}
