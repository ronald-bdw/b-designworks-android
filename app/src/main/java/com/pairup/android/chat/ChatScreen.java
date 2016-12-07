package com.pairup.android.chat;

import android.content.Intent;
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
import android.widget.Toast;

import com.anjlab.android.iab.v3.TransactionDetails;
import com.pairup.android.R;
import com.pairup.android.UserInteractor;
import com.pairup.android.subscription.SubscriptionPresenter;
import com.pairup.android.subscription.SubscriptionView;
import com.pairup.android.utils.AndroidUtils;
import com.pairup.android.utils.Bus;
import com.pairup.android.utils.Keyboard;
import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.ui.SimpleDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.smooch.core.Smooch;
import io.smooch.core.User;
import io.smooch.ui.ConversationActivity;
import rx.functions.Action1;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class ChatScreen extends ConversationActivity implements SubscriptionView {

    @Inject UserInteractor        userInteractor;
    @Inject SubscriptionPresenter subscriptionPresenter;

    @Bind(R.id.drawer)                     DrawerLayout uiDrawer;
    @Bind(R.id.provider_logo)              ImageView    uiProviderLogo;
    @Bind(R.id.buy_subscription_container) View         uiBuySubscription;

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);

        customizeSmoochInterface();

        if (savedState == null) {
            if (userInteractor.firstVisitAfterLogin()) {
                Smooch.logout();
                userInteractor.trackFirstVisit();
            }
            com.pairup.android.login.models.User user = userInteractor.getUser();

            Smooch.login(userInteractor.getUserZendeskId(), null);
            User.getCurrentUser().setEmail(user.getEmail());
            User.getCurrentUser().setFirstName(user.getFirstName());
            User.getCurrentUser().setLastName(user.getId());
            userInteractor.sendNotificationsStatus();

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

     private void setChatGone(boolean gone) {
        if (gone) {
            uiBuySubscription.setVisibility(View.VISIBLE);
        } else {
            uiBuySubscription.setVisibility(View.INVISIBLE);
        }
    }

    private void customizeSmoochInterface() {
        ViewGroup rootView = ((ViewGroup) findViewById(android.R.id.content));
        View oldChatLayout = rootView.getChildAt(0);
        rootView.removeView(oldChatLayout);

        View newChatLayout = LayoutInflater.from(this).inflate(R.layout.screen_chat, rootView);
        ((ViewGroup) newChatLayout.findViewById(R.id.chat_container)).addView(oldChatLayout, 1);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.menu) void onMenuClick() {
        if (uiDrawer.isDrawerOpen(GravityCompat.END)) {
            ChatScreen.this.closeDrawer();
        } else {
            uiDrawer.openDrawer(GravityCompat.END);
        }
    }

    @OnClick(R.id.subscribe) void onSubscribeClick() {
        subscriptionPresenter.showSubscriptionDialog();
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
        subscriptionPresenter.attachView(this, this);
        setChatGone(!(subscriptionPresenter.isSubscribed() || userInteractor.getUser().hasHbfProvider()));

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
        if (userInteractor.getUser().hasHbfProvider()) {
            uiProviderLogo.setVisibility(View.VISIBLE);
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

    @Override public void onStop() {
        subscriptionPresenter.detachView();
        super.onStop();
    }

    private void closeDrawer() {
        uiDrawer.closeDrawer(GravityCompat.END);
    }

    @Override public void onProductPurchased(String productId, TransactionDetails details) {
        Toast.makeText(this, R.string.subscription_owned_text, Toast.LENGTH_LONG).show();
    }

    @Override public void showSubscriptionDialog() {
        SimpleDialog.showList(this,
            getString(R.string.subscriptions),
            getResources().getStringArray(R.array.subscriptions),
            new Action1<Integer>() {
                @Override public void call(Integer integer) {
                    subscriptionPresenter.subscribe(integer);
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!subscriptionPresenter.getBillingProcessor().handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }
}