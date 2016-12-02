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
import com.pairup.android.utils.ui.SimpleDialog;
import com.pairup.android.utils.ui.UiInfo;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 22.08.2016.
 */
public class WelcomeScreen extends BaseActivity {

    public static final String ARG_HAS_ERROR           = "hasError";
    public static final String ARG_IS_PHONE_REGISTERED = "isPhoneRegistered";

    private boolean hasError;
    private boolean isPhoneRegistered;

    @Bind(R.id.have_account_link) TextView uiHaveAccountLink;

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
        Navigator.enterPhone(context(), AccountVerificationType.IS_REGISTERED);
    }

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        underlineHaveAccountLinkText();

        if(hasError){
            String message;
            if(isPhoneRegistered){
                message = getString(R.string.someone_accessed_your_account_message);
            } else {
                message = getString(R.string.account_deleted_message);
            }
            SimpleDialog.show(this, getString(R.string.error), message, getString(R.string.ok), null);
        }
    }

    @Override
    protected void parseArguments(@NonNull Bundle extras) {
        hasError = extras.getBoolean(ARG_HAS_ERROR, false);
        isPhoneRegistered = extras.getBoolean(ARG_IS_PHONE_REGISTERED, true);
    }

    private void underlineHaveAccountLinkText() {
        SpannableString content = new SpannableString(mHaveAccountLinkTxt);
        content.setSpan(new UnderlineSpan(), 0, mHaveAccountLinkTxt.length(), 0);
        uiHaveAccountLink.setText(content);
    }
}
