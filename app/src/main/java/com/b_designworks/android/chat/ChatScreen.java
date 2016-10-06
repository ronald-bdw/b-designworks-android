package com.b_designworks.android.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.utils.Bus;
import com.b_designworks.android.utils.Keyboard;
import com.b_designworks.android.utils.di.Injector;
import com.b_designworks.android.utils.ui.UiInfo;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.Bind;
import io.smooch.core.Smooch;
import io.smooch.core.User;
import io.smooch.ui.fragment.ConversationFragment;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class ChatScreen extends BaseActivity {

    private static final String TAG = "ChatScreen";

    @Inject UserInteractor userInteractor;

    @Bind(R.id.drawer) DrawerLayout uiDrawer;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_chat).setMenuRes(R.menu.chat);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
        if (savedState == null) {
            if (userInteractor.firstVisitAfterLogin()) {
                Smooch.logout();
                userInteractor.trackFirstVisit();
            }
            com.b_designworks.android.login.models.User user = userInteractor.getUser();

            Smooch.login(userInteractor.getUserId(), null);
            User.getCurrentUser().setEmail(user.getEmail());
            User.getCurrentUser().setFirstName(user.getFirstName());
            User.getCurrentUser().setLastName(user.getId());

            Log.d(TAG, "Current user id  is: " + userInteractor.getUserId());
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.chat_container, new ConversationFragment())
                .replace(R.id.side_panel_container, new ChatSidePanelFragment())
                .commit();
        }
        uiDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override public void onDrawerSlide(View drawerView, float slideOffset) {
                Keyboard.hide(ChatScreen.this);
            }

            @Override public void onDrawerOpened(View drawerView) {

            }

            @Override public void onDrawerClosed(View drawerView) {

            }

            @Override public void onDrawerStateChanged(int newState) {

            }
        });

    }

    @Override public void onBackPressed() {
        if (uiDrawer.isDrawerOpen(GravityCompat.END)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override protected void onResume() {
        super.onResume();
        Bus.subscribe(this);
    }

    @Subscribe public void onEvent(CloseDrawerEvent event) {
        closeDrawer();
    }

    @Override protected void onPause() {
        Bus.unsubscribe(this);
        super.onPause();
    }

    private void closeDrawer() {
        uiDrawer.closeDrawer(GravityCompat.END);
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