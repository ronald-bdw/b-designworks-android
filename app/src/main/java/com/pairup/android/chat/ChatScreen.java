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
import com.google.android.gms.common.ConnectionResult;
import com.pairup.android.Navigator;
import com.pairup.android.R;
import com.pairup.android.UserInteractor;
import com.pairup.android.subscription.SubscriptionChangeEvent;
import com.pairup.android.subscription.SubscriptionDialog;
import com.pairup.android.subscription.SubscriptionDialogItemClickEvent;
import com.pairup.android.subscription.SubscriptionPresenter;
import com.pairup.android.subscription.SubscriptionView;
import com.pairup.android.sync.GoogleFitPresenter;
import com.pairup.android.sync.GoogleFitView;
import com.pairup.android.utils.Analytics;
import com.pairup.android.utils.AndroidUtils;
import com.pairup.android.utils.Bus;
import com.pairup.android.utils.Keyboard;
import com.pairup.android.utils.Logger;
import com.pairup.android.utils.Times;
import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.network.ErrorUtils;
import com.pairup.android.utils.ui.SimpleDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.smooch.core.Message;
import io.smooch.core.MessageUploadStatus;
import io.smooch.ui.ConversationActivity;
import rx.functions.Action0;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class ChatScreen extends ConversationActivity implements SubscriptionView, ChatView,
    GoogleFitView {

    public static final String ARG_NEED_GOOGLE_FIT_INTEGRATION = "needGoogleFitIntegration";

    @Inject UserInteractor        userInteractor;
    @Inject SubscriptionPresenter subscriptionPresenter;
    @Inject ChatPresenter         chatPresenter;
    @Inject GoogleFitPresenter    googleFitPresenter;

    @Bind(R.id.drawer)                     DrawerLayout uiDrawer;
    @Bind(R.id.buy_subscription_container) View         uiBuySubscription;

    private boolean needGoogleFitIntegration;

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);

        if (getIntent() != null) {
            needGoogleFitIntegration = getIntent()
                .getBooleanExtra(ARG_NEED_GOOGLE_FIT_INTEGRATION, false);
        }

        chatPresenter.initialization();

        Analytics.logScreenOpened(Analytics.EVENT_OPEN_CHAT_SCREEN);

        customizeSmoochInterface();

        initSidePanel();

        if (savedState == null) {
            chatPresenter.initSmooch();
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

        userInteractor.sendTimeZoneToServer(Times.getTimeZone());

        if (userInteractor.getUser().isFirstPopupActive()) {
            SimpleDialog
                .withOkBtn(this, userInteractor.getUser().getProvider().getFirstPopupMessage());
        }

        if (needGoogleFitIntegration) {
            googleFitPresenter.attachView(this, this);
        }
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
        chatPresenter.onViewShown(this);
        subscriptionPresenter.attachView(this, this);
        setChatGone(!(subscriptionPresenter.isSubscribed() ||
            userInteractor.getUser().hasProvider()));

        // we could not customize part of the UI in on create
        // because not all necessary views present in the hierarcy
        // that's the reason why we split customize process between onCreate/onResume
        findViewById(R.id.scrollView).setBackgroundResource(R.drawable.white_with_round_corners);
        View uiInputText = findViewById(R.id.Smooch_inputText);
        uiInputText.setBackgroundColor(Color.WHITE);
        RelativeLayout.LayoutParams layoutParams =
            (RelativeLayout.LayoutParams) uiInputText.getLayoutParams();
        layoutParams.topMargin = AndroidUtils.dp(8);
        layoutParams.bottomMargin = AndroidUtils.dp(8);
        layoutParams.leftMargin = AndroidUtils.dp(40);

        layoutParams.addRule(RelativeLayout.RIGHT_OF, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        ((View) uiInputText.getParent()).setBackgroundColor(0xFFECF3FA);
        ((ImageView) findViewById(R.id.Smooch_btnSend)).setImageResource(R.drawable.ic_send);

        showUserName(userInteractor.getFullName());
    }

    private void showUserName(String username) {
        ((TextView) findViewById(R.id.full_name)).setText(username);
    }

    @Subscribe public void onEvent(CloseDrawerEvent event) {
        closeDrawer();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserProfileUpdatedEvent event) {
        showUserName(userInteractor.getFullName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SubscriptionChangeEvent event) {
        setChatGone(!(subscriptionPresenter.isSubscribed() ||
            userInteractor.getUser().hasProvider()));
    }

    @Override public void onPause() {
        Bus.unsubscribe(this);
        subscriptionPresenter.onViewHidden();
        chatPresenter.onViewHidden();
        super.onPause();
    }

    @Override public void onStop() {
        googleFitPresenter.detachView(this);
        super.onStop();
    }

    private void closeDrawer() {
        uiDrawer.closeDrawer(GravityCompat.END);
    }

    @Override public void onProductPurchased(String productId, TransactionDetails details) {
        Toast.makeText(this, R.string.subscription_owned_text, Toast.LENGTH_LONG).show();
    }

    @Override public void showSubscriptionDialog() {
        SubscriptionDialog.show(this);
    }

    @Subscribe(sticky = true) public void onEvent(SubscriptionDialogItemClickEvent subscription) {
        subscriptionPresenter.subscribe(subscription);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!subscriptionPresenter.getBillingProcessor()
            .handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
            googleFitPresenter.handleResponse(requestCode, resultCode, data);
        }
    }

    @Override public void onMessageSent(Message message, MessageUploadStatus messageUploadStatus) {
        super.onMessageSent(message, messageUploadStatus);
        Analytics.logUserResponseSpeed();

        if (userInteractor.getUser().isSecondPopupActive() &&
            !subscriptionPresenter.isSubscribed()) {

            SimpleDialog
                .withOkBtn(this, userInteractor.getUser().getProvider().getSecondPopupMessage());
        }
    }

    @Override
    public void openWelcomeScreenWithError(boolean isPhoneRegistered) {
        Navigator.welcomeWithError(ChatScreen.this, isPhoneRegistered);
    }

    public void initSidePanel() {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.side_panel_container, new ChatSidePanelFragment())
            .commit();
    }

    @Override public void integrationSuccessful() {
        Toast.makeText(this, R.string.google_fit_token_retrieved, Toast.LENGTH_SHORT).show();
    }

    @Override public void errorWhileRetrievingCode() {
        Toast.makeText(this, R.string.google_fit_fail_to_retrieve_token, Toast.LENGTH_SHORT).show();
    }

    @Override public void onGoogleServicesError(ConnectionResult result) {
        SimpleDialog.show(this,
            getString(R.string.error),
            getString(R.string.google_play_services_connecting_exeption) +
                result.getErrorMessage(),
            getString(R.string.ok),
            new Action0() {
                @Override public void call() {
                }
            });
    }

    @Override public void showInternetConnectionError() {
        SimpleDialog.networkProblem(this);
    }

    @Override public void showGoogleServiceDisconnected() {
        SimpleDialog.show(this,
            getString(R.string.error),
            getString(R.string.google_play_services_disconnected),
            getString(R.string.ok),
            new Action0() {
                @Override public void call() {
                }
            });
    }

    @Override public void onError(Throwable error) {
        ErrorUtils.handle(this, error);
    }

    @Override public void userCancelIntegration() {
        Logger.dToast(this, "User cancel google fit integration");
    }

    @Override public void onClientConnected() {
        googleFitPresenter.logout();
        googleFitPresenter.startIntegrate(this);
        needGoogleFitIntegration = false;
    }
}