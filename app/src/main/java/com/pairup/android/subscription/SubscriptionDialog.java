package com.pairup.android.subscription;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.pairup.android.R;
import com.pairup.android.utils.Bus;
import com.pairup.android.utils.ui.BaseDialogFragment;
import com.pairup.android.utils.ui.UiInfo;

import butterknife.OnClick;

/**
 * Created by almaziskhakov on 10/02/2017.
 */

public class SubscriptionDialog extends BaseDialogFragment {

    private static final String ARG_WITH_TRIAL = "withTrial";

    private boolean withTrial;

    public static void show(FragmentActivity activity, boolean withTrial) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_WITH_TRIAL, withTrial);
        SubscriptionDialog subscriptionDialog = new SubscriptionDialog();
        subscriptionDialog.setArguments(args);
        show(subscriptionDialog, activity);
    }

    @Override protected UiInfo getUiInfo() {
        return new UiInfo(R.layout.dialog_subscription);
    }

    @Override protected void parseArguments(@NonNull Bundle args) {
        withTrial = args.getBoolean(ARG_WITH_TRIAL, true);
    }

    @OnClick(R.id.starter) void onStartClick() {
        subscribe(SubscriptionDialogItemClickEvent.STARTER,
            SubscriptionDialogItemClickEvent.STARTER_WITHOUT_TRIAL);
    }

    @OnClick(R.id.stabilizer) void onStabilizerClick() {
        subscribe(SubscriptionDialogItemClickEvent.STABILIZER,
            SubscriptionDialogItemClickEvent.STABILIZER_WITHOUT_TRIAL);
    }

    @OnClick(R.id.master) void onMasterClick() {
        subscribe(SubscriptionDialogItemClickEvent.MASTER,
            SubscriptionDialogItemClickEvent.MASTER_WITHOUT_TRIAL);
    }

    private void subscribe(@NonNull SubscriptionDialogItemClickEvent subscriptionWithTrial,
                           @NonNull SubscriptionDialogItemClickEvent subscriptionWithoutTrial) {
        Bus.event(withTrial ? subscriptionWithTrial : subscriptionWithoutTrial);
        getDialog().dismiss();
    }

}
