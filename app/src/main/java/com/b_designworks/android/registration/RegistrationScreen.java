package com.b_designworks.android.registration;

import android.support.annotation.NonNull;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.UiInfo;

/**
 * Created by Ilya Eremin on 09.08.2016.
 */
public class RegistrationScreen extends BaseActivity {

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_registration);
    }
}
