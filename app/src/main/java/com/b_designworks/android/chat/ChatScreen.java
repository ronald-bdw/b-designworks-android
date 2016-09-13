package com.b_designworks.android.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.DI;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.login.models.User;
import com.b_designworks.android.utils.ImageLoader;
import com.b_designworks.android.utils.ui.UiInfo;

import butterknife.Bind;
import butterknife.OnClick;
import io.smooch.core.Smooch;
import io.smooch.ui.fragment.ConversationFragment;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class ChatScreen extends BaseActivity {

    private static final String TAG = "ChatScreen";

    private UserInteractor userInteractor = DI.getInstance().getUserInteractor();

    @Bind(R.id.drawer)    DrawerLayout uiDrawer;
    @Bind(R.id.avatar)    ImageView    uiAvatar;
    @Bind(R.id.full_name) TextView     uiFullname;
    @Bind(R.id.email)     TextView     uiEmail;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_chat).setMenuRes(R.menu.chat);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        if (savedState == null) {
            if (userInteractor.firstVisitAfterLogin()) {
                Smooch.logout();
                userInteractor.trackFirstVisit();
            }
            Smooch.login(userInteractor.getUserId(), null);
            Log.d(TAG, "Current user id  is: " + userInteractor.getUserId());
            getSupportFragmentManager().beginTransaction().replace(R.id.chat_container, new ConversationFragment()).commit();
        }
        User user = userInteractor.getUser();
        ImageLoader.load(context(), uiAvatar, user.getAvatar().getOriginal());
        uiFullname.setText(getString(R.string.edit_profile_name_surname_pattern, user.getFirstName(), user.getLastName()));
        uiEmail.setText(user.getEmail());
    }

    @Override public void onBackPressed() {
        if (uiDrawer.isDrawerOpen(GravityCompat.END)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void closeDrawer() {
        uiDrawer.closeDrawer(GravityCompat.END);
    }

    @OnClick(R.id.profile) void onProfileClick() {
        closeDrawer();
        Navigator.profile(context());
    }

    @OnClick(R.id.settings) void onSettingsClick() {
        closeDrawer();
        Navigator.settings(context());
    }

    @OnClick(R.id.about_us) void onAboutUsClick() {
        closeDrawer();
        Navigator.aboutUs(context());
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.right_panel:
                if (uiDrawer.isDrawerOpen(GravityCompat.END)) {
                    closeDrawer();
                } else {
                    uiDrawer.openDrawer(GravityCompat.END);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}