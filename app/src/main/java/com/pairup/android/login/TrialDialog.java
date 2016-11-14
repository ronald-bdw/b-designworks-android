package com.pairup.android.login;

import com.pairup.android.BaseActivity;
import com.pairup.android.Navigator;
import com.pairup.android.R;
import com.pairup.android.utils.ui.BaseDialogFragment;
import com.pairup.android.utils.ui.UiInfo;

import butterknife.OnClick;

/**
 * Created by Klymenko on 11.11.2016.
 */

public class TrialDialog extends BaseDialogFragment {

    public static void show(BaseActivity activity) {
        show(new TrialDialog(), activity);
    }

    @Override protected UiInfo getUiInfo() {
        return new UiInfo(R.layout.dialog_trial);
    }

    @OnClick(R.id.start_trial_now) void onStartTrialClick() {
        Navigator.enterPhone(context(), AccountVerificationType.IS_NOT_REGISTERED);
    }

    @OnClick(R.id.learn_more) void onLearnMoreClick() {
        Navigator.trialPage(context());
    }
}
