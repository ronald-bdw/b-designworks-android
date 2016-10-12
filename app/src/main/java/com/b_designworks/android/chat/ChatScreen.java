package com.b_designworks.android.chat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.b_designworks.android.R;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.utils.AndroidUtils;
import com.b_designworks.android.utils.Bus;
import com.b_designworks.android.utils.Keyboard;
import com.b_designworks.android.utils.di.Injector;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import io.smooch.core.Smooch;
import io.smooch.core.User;
import io.smooch.ui.ConversationActivity;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class ChatScreen extends ConversationActivity {

    @Inject UserInteractor userInteractor;

    private DrawerLayout uiDrawer;
    private View         uiActionBar;

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);

        addRightPanel();

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
                .replace(R.id.chat_right_panel, new ChatSidePanelFragment())
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

    private void addRightPanel() {
        uiDrawer = new DrawerLayout(this);

        final FrameLayout rightSidePanelCountainer = new FrameLayout(this);
        final FrameLayout chatContainer = new FrameLayout(this);

        rightSidePanelCountainer.setId(R.id.chat_right_panel);
        DrawerLayout.LayoutParams lp = new DrawerLayout.LayoutParams(AndroidUtils.dp(240), LinearLayout.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.END;
        rightSidePanelCountainer.setLayoutParams(lp);

        ViewGroup contentView = ((ViewGroup) findViewById(android.R.id.content));
        ViewGroup rootView = (ViewGroup) contentView.getChildAt(0);
        contentView.removeView(rootView);
        rootView.setBackgroundColor(Color.WHITE);
        chatContainer.addView(rootView, new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT));
        uiActionBar = LayoutInflater.from(this).inflate(R.layout.layout_chat_action_bar, chatContainer);
        uiActionBar.findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View onClick) {
                if (uiDrawer.isDrawerOpen(GravityCompat.END)) {
                    ChatScreen.this.closeDrawer();
                } else {
                    uiDrawer.openDrawer(GravityCompat.END);
                }
            }
        });
        // TODO decide show or not hbf logo, need info from server side

        uiDrawer.addView(chatContainer, new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT));
        uiDrawer.addView(rightSidePanelCountainer);
        contentView.addView(uiDrawer);
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
        // the only way to customize smooch UI
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
        Bus.subscribe(this);
    }

    @Subscribe public void onEvent(CloseDrawerEvent event) {
        closeDrawer();
    }

    @Override public void onPause() {
        Bus.unsubscribe(this);
        super.onPause();
    }

    private void closeDrawer() {
        uiDrawer.closeDrawer(GravityCompat.END);
    }
}