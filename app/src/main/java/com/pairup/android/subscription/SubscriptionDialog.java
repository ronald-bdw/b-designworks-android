package com.pairup.android.subscription;

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

    public static void show(FragmentActivity activity) {
        show(new SubscriptionDialog(), activity);
    }

    @Override protected UiInfo getUiInfo() {
        return new UiInfo(R.layout.dialog_subscription);
    }

    @OnClick(R.id.starter) void onStartClick() {
        Bus.event(SubscriptionDialogItemTabEvent.STARTER);
        getDialog().dismiss();
    }

    @OnClick(R.id.stabilizer) void onStabilizerClick() {
        Bus.event(SubscriptionDialogItemTabEvent.STABILIZER);
        getDialog().dismiss();
    }

    @OnClick(R.id.master) void onMasterClick() {
        Bus.event(SubscriptionDialogItemTabEvent.MASTER);
        getDialog().dismiss();
    }

}
