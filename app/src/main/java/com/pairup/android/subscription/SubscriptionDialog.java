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

    private static void show(FragmentActivity activity, boolean withTrial) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_WITH_TRIAL, withTrial);
        SubscriptionDialog subscriptionDialog = new SubscriptionDialog();
        subscriptionDialog.setArguments(args);
        show(subscriptionDialog, activity);
    }

    public static void showWithTrial(FragmentActivity activity) {
        show(activity, true);
    }

    public static void showWithoutTrial(FragmentActivity activity) {
        show(activity, false);
    }

    @Override protected UiInfo getUiInfo() {
        return new UiInfo(R.layout.dialog_subscription);
    }

    @Override protected void parseArguments(@NonNull Bundle args) {
        withTrial = args.getBoolean(ARG_WITH_TRIAL, true);
    }

    @OnClick(R.id.starter) void onStartClick() {
        Bus.event(withTrial ?
            SubscriptionDialogItemClickEvent.STARTER :
            SubscriptionDialogItemClickEvent.STARTER_WITHOUT_TRIAL);
        getDialog().dismiss();
    }

    @OnClick(R.id.stabilizer) void onStabilizerClick() {
        Bus.event(withTrial ?
            SubscriptionDialogItemClickEvent.STABILIZER :
            SubscriptionDialogItemClickEvent.STABILIZER_WITHOUT_TRIAL);
        getDialog().dismiss();
    }

    @OnClick(R.id.master) void onMasterClick() {
        Bus.event(withTrial ?
            SubscriptionDialogItemClickEvent.MASTER :
            SubscriptionDialogItemClickEvent.MASTER_WITHOUT_TRIAL);
        getDialog().dismiss();
    }

}
