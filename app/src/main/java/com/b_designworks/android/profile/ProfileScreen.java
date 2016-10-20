package com.b_designworks.android.profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.login.models.User;
import com.b_designworks.android.utils.Bus;
import com.b_designworks.android.utils.ImageLoader;
import com.b_designworks.android.utils.Logger;
import com.b_designworks.android.utils.Rxs;
import com.b_designworks.android.utils.di.Injector;
import com.b_designworks.android.utils.network.ErrorUtils;
import com.b_designworks.android.utils.ui.UiInfo;

import javax.inject.Inject;

import butterknife.Bind;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class ProfileScreen extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Inject UserInteractor userInteractor;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_profile).enableBackButton().setMenuRes(R.menu.profile);
    }

    @Bind(R.id.avatar)            ImageView          uiAvatar;
    @Bind(R.id.email)             TextView           uiEmail;
    @Bind(R.id.phone)             TextView           uiPhone;
    @Bind(R.id.current_full_name) TextView           uiCurrentFullName;
    @Bind(R.id.current_email)     TextView           uiCurrentEmail;
    @Bind(R.id.swipe_to_refresh)  SwipeRefreshLayout uiSwipeRefreshLayout;

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
        tuneSwipeRefreshLayout();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            Navigator.editProfile(context());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override protected void onResume() {
        super.onResume();
        showUser(userInteractor.getUser());
    }

    @Override public void onRefresh() {
        uiSwipeRefreshLayout.setRefreshing(true);
        userInteractor.updateUserProfile()
            .compose(Rxs.doInBackgroundDeliverToUI())
            .doOnTerminate(() -> uiSwipeRefreshLayout.setRefreshing(false))
            .subscribe(this::showUser, ErrorUtils.handle(context()));
    }

    private void tuneSwipeRefreshLayout() {
        uiSwipeRefreshLayout.setOnRefreshListener(this);
        uiSwipeRefreshLayout.setColorSchemeResources(R.color.app_accent);
    }

    private void showUser(User user) {
        if (context() != null) {
            ImageLoader.load(this, uiAvatar, user.getAvatar().getThumb());
            uiCurrentFullName.setText(getString(R.string.edit_profile_name_surname_pattern, user.getFirstName(), user.getLastName()));
            uiEmail.setText(user.getEmail());
            uiCurrentEmail.setText(user.getEmail());
            uiPhone.setText(user.getPhoneNumber());
            setTitle(user.getFirstName());
        }
    }
}
