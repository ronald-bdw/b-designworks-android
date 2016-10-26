package com.b_designworks.android.chat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.utils.AndroidUtils;
import com.b_designworks.android.utils.Bus;
import com.b_designworks.android.utils.Keyboard;
import com.b_designworks.android.utils.di.Injector;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.smooch.core.Smooch;
import io.smooch.core.User;
import io.smooch.ui.ConversationActivity;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class ChatScreen extends ConversationActivity {

    @Inject UserInteractor userInteractor;

    @Bind(R.id.drawer)        DrawerLayout uiDrawer;
    @Bind(R.id.provider_logo) ImageView    uiProviderLogo;

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);

        customizeSmoochInterface();

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

            getSupportFragmentManager().beginTransaction()
                .replace(R.id.side_panel_container, new ChatSidePanelFragment())
                .commit();
        }
        uiDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override public void onDrawerSlide(View drawerView, float slideOffset) {
                Keyboard.hide(ChatScreen.this);
            }

            @Override public void onDrawerOpened(View drawerView) {}

            @Override public void onDrawerClosed(View drawerView) {}

            @Override public void onDrawerStateChanged(int newState) {}
        });
    }

    private void customizeSmoochInterface() {
        ViewGroup rootView = ((ViewGroup) findViewById(android.R.id.content));
        View oldChatLayout = rootView.getChildAt(0);
        rootView.removeView(oldChatLayout);

        View newChatLayout = LayoutInflater.from(this).inflate(R.layout.screen_chat, rootView);
        ((ViewGroup) newChatLayout.findViewById(R.id.chat_container)).addView(oldChatLayout);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.menu) void onMenuClick() {
        if (uiDrawer.isDrawerOpen(GravityCompat.END)) {
            ChatScreen.this.closeDrawer();
        } else {
            uiDrawer.openDrawer(GravityCompat.END);
        }
    }

    @Override public void onBackPressed() {
        if (uiDrawer.isDrawerOpen(GravityCompat.END)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override public void onResume() {
        super.onResume();
        Bus.subscribe(this);

        // we could not customize part of the UI in on create because not all necessary views present in the hierarcy
        // that's the reason why we split customize process between onCreate/onResume
        findViewById(R.id.scrollView).setBackgroundResource(R.drawable.white_with_round_corners);
        View uiInputText = findViewById(R.id.Smooch_inputText);
        uiInputText.setBackgroundColor(Color.WHITE);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) uiInputText.getLayoutParams();
        layoutParams.topMargin = AndroidUtils.dp(8);
        layoutParams.bottomMargin = AndroidUtils.dp(8);
        layoutParams.leftMargin = AndroidUtils.dp(8);

        layoutParams.addRule(RelativeLayout.RIGHT_OF, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        ((View) uiInputText.getParent()).setBackgroundColor(0xFFECF3FA);
        findViewById(R.id.Smooch_btnCamera).setVisibility(View.GONE);
        ((ImageView) findViewById(R.id.Smooch_btnSend)).setImageResource(R.drawable.ic_send);

        showUserName(userInteractor.getFullName());
        setUpProviderLogo();
    }

    private void showUserName(String username) {
        ((TextView) findViewById(R.id.full_name)).setText(username);
    }

    private void setUpProviderLogo() {
        com.b_designworks.android.login.models.User user = userInteractor.getUser();
        if (user.getProvider() != null) {
            if (user.getProvider().getName() != null) {
                if ("HBF".equals(userInteractor.getUser().getProvider().getName())) {
                    uiProviderLogo.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Subscribe public void onEvent(CloseDrawerEvent event) {
        closeDrawer();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserProfileUpdatedEvent event) {
        showUserName(userInteractor.getFullName());
    }

    @Override public void onPause() {
        Bus.unsubscribe(this);
        super.onPause();
    }

    private void closeDrawer() {
        uiDrawer.closeDrawer(GravityCompat.END);
    }
}