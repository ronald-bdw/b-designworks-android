package com.pairup.android.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import com.pairup.android.BaseActivity;
import com.pairup.android.Navigator;
import com.pairup.android.R;
import com.pairup.android.utils.ui.BaseDialogFragment;
import com.pairup.android.utils.ui.UiInfo;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 22.08.2016.
 */
public class WelcomeScreen extends BaseActivity {

    @Bind(R.id.have_account_link) TextView mHaveAccountLink;

    @BindString(R.string.screen_welcome_user_have_account) String mHaveAccountLinkTxt;

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
        Navigator.enterPhoneAndVerify(context(), true);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);

        SpannableString content = new SpannableString(mHaveAccountLinkTxt);
        content.setSpan(new UnderlineSpan(), 0, mHaveAccountLinkTxt.length(), 0);
        mHaveAccountLink.setText(content);
    }

    public static class TrialDialog extends BaseDialogFragment {

        public static void show(BaseActivity activity) {
            show(new TrialDialog(), activity);
        }

        @Override protected UiInfo getUiInfo() {
            return new UiInfo(R.layout.dialog_trial);
        }

        @OnClick(R.id.start_trial_now) void onStartTrialClick() {
            Navigator.enterPhoneAndVerify(context());
        }

        @OnClick(R.id.learn_more) void onLearnMoreClick() {
            Navigator.trialPage(context());
        }
    }
}
