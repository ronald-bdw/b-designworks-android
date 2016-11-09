package com.pairup.android.subscription;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.TransactionDetails;
import com.pairup.android.BaseActivity;
import com.pairup.android.R;
import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.ui.UiInfo;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 10/21/16.
 */

public class SubscriptionScreen extends BaseActivity implements SubscriptionView {

    @Inject SubscriptionPresenter subscriptionPresenter;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_subscription)
            .setTitleRes(R.string.subscription)
            .enableBackButton();
    }
    @Bind(R.id.status) TextView uiStatus;

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
        subscriptionPresenter.attachView(this, this);
        uiStatus.setText(subscriptionPresenter.getSubsciptionStatus());
    }

    @OnClick(R.id.subscribe) void onSubscribeClick() {
        subscriptionPresenter.subscribe();
    }

    @OnClick(R.id.unsubscribe) void onUnsubscribeClick() {
        subscriptionPresenter.unsubscribe(this);
    }

    @Override public void onProductPurchased(String productId, TransactionDetails details) {
        Toast.makeText(this, R.string.subscription_owned_text, Toast.LENGTH_LONG).show();
    }

    @Override protected void onDestroy() {
        subscriptionPresenter.detachView();
        super.onDestroy();
    }
}
