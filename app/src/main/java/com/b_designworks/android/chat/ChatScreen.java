package com.b_designworks.android.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.Fakes;
import com.b_designworks.android.utils.UiInfo;
import com.b_designworks.android.utils.ui.SpaceDecorator;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class ChatScreen extends BaseActivity {

    @Bind(R.id.list)   RecyclerView uiChatList;
    @Bind(R.id.drawer) DrawerLayout uiDrawer;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_chat).setMenuRes(R.menu.chat);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        uiChatList.setLayoutManager(new LinearLayoutManager(context()));
        uiChatList.addItemDecoration(new SpaceDecorator(0, 8, 0, 8));
        uiChatList.setAdapter(new ChatAdapter(Fakes.createMessages()));
    }

    @Override public void onBackPressed() {
        if (uiDrawer.isDrawerOpen(GravityCompat.END)) {
            uiDrawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.profile) void onProfileClick() {
        Navigator.profile(context());
    }

    @OnClick(R.id.settings) void onSettingsClick() {
        Navigator.settings(context());
    }

    @OnClick(R.id.push_notifications) void onPushNotificationsClick() {
        Navigator.pushNotifications(context());
    }

    @OnClick(R.id.about_us) void onAboutUsClick() {
        Navigator.aboutUs(context());
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.right_panel:
                if (uiDrawer.isDrawerOpen(GravityCompat.END)) {
                    uiDrawer.closeDrawer(GravityCompat.END);
                } else {
                    uiDrawer.openDrawer(GravityCompat.END);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}