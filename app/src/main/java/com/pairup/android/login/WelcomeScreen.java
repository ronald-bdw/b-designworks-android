package com.pairup.android.login;

import android.support.annotation.NonNull;

import com.pairup.android.BaseActivity;
import com.pairup.android.Navigator;
import com.pairup.android.R;
import com.pairup.android.utils.ui.BaseDialogFragment;
import com.pairup.android.utils.ui.UiInfo;

import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 22.08.2016.
 */
public class WelcomeScreen extends BaseActivity {

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_welcome);
    }

    @OnClick(R.id.yes) void onYesClick() {
        Navigator.selectProvider(context());
    }

    @OnClick(R.id.no) void onNoClick() {
        TrialDialog.show(this);
    }

    @OnClick(R.id.have_account_link) void haveAccountClick() {
        Navigator.enterPhone(context());
    }

    public static class TrialDialog extends BaseDialogFragment {

        public static void show(BaseActivity activity) {
            show(new TrialDialog(), activity);
        }

        @Override protected UiInfo getUiInfo() {
            return new UiInfo(R.layout.dialog_trial);
        }

        @OnClick(R.id.start_trial_now) void onStartTrialClick() {
            Navigator.enterPhone(context());
        }

        @OnClick(R.id.learn_more) void onLearnMoreClick() {
            Navigator.trialPage(context());
        }
    }
}
