package com.b_designworks.android.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.DI;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.UserManager;
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

    private UserManager userManager = DI.getInstance().getUserManager();

    @Bind(R.id.drawer) DrawerLayout uiDrawer;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_chat).setMenuRes(R.menu.chat);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        if (savedState == null) {
            if (userManager.firstVisitAfterLogin()) {
                Smooch.logout();
                userManager.trackFirstVisit();
            }
            Smooch.login(userManager.getUserId(), null);
            Log.d(TAG, "Current user id  is: " + userManager.getUserId());
            getSupportFragmentManager().beginTransaction().replace(R.id.chat_container, new ConversationFragment()).commit();
        }
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

    @OnClick(R.id.push_notifications) void onPushNotificationsClick() {
        closeDrawer();
        Navigator.pushNotifications(context());
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