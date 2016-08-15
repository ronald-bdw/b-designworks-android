package com.b_designworks.android.login;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.BuildConfig;
import com.b_designworks.android.DI;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.Rxs;
import com.b_designworks.android.utils.network.ErrorUtils;
import com.b_designworks.android.utils.ui.TextViews;
import com.b_designworks.android.utils.ui.UiInfo;

import butterknife.Bind;
import butterknife.OnClick;
import rx.Subscription;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class EnterPhoneScreen extends BaseActivity {

    @Bind(R.id.phone) EditText uiPhone;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_enter_phone);
    }

    @SuppressLint("SetTextI18n") @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        if (BuildConfig.DEBUG) {
            uiPhone.setText("+79625535458");
        }
    }

    @Nullable ProgressDialog progresDialog;
    @Nullable Subscription   sendingSubscription;

    @OnClick(R.id.next) void onNextClick() {
        if(sendingSubscription != null) return;
        progresDialog = ProgressDialog.show(context(), getString(R.string.loading), getString(R.string.loading_sending_code));
        sendingSubscription = DI.getInstance().getApi().getCode(TextViews.textOf(uiPhone))
            .compose(Rxs.doInBackgroundDeliverToUI())
            .doOnEach(i -> {
                if (progresDialog != null) {
                    progresDialog.dismiss();
                }
                sendingSubscription = null;
            })
            .subscribe(result -> {
                if (result.isPhoneRegistered()) {
                    Navigator.selectProvider(context());
                } else {
                    Navigator.registration(context(), TextViews.textOf(uiPhone), result.getPhoneCodeId());
                }
            }, ErrorUtils.onError());
    }

    @Override protected void onStop() {
        if (sendingSubscription != null && !sendingSubscription.isUnsubscribed()) {
            sendingSubscription.unsubscribe();
        }
        super.onStop();
    }

}
