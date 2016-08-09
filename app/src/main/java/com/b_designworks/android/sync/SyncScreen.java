package com.b_designworks.android.sync;

import android.support.annotation.NonNull;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.UiInfo;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class SyncScreen extends BaseActivity {

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_sync).enableBackButton().setTitleRes(R.string.title_sync);
    }
}
