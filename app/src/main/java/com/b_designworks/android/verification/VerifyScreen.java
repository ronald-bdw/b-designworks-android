package com.b_designworks.android.verification;

import android.support.annotation.NonNull;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.Navigator;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.UiInfo;

import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 03.08.2016.
 */
public class VerifyScreen extends BaseActivity {
    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_verify);
    }

    @OnClick(R.id.next) void onNextClick(){
        Navigator.chat(context());
    }
}
