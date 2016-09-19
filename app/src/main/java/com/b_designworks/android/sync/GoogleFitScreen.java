package com.b_designworks.android.sync;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.b_designworks.android.BaseActivity;
import com.b_designworks.android.R;
import com.b_designworks.android.utils.di.Injector;
import com.b_designworks.android.utils.ui.UiInfo;
import com.google.android.gms.common.api.GoogleApiClient;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Ilya Eremin on 24.08.2016.
 */
public class GoogleFitScreen extends BaseActivity {

    private static final String TAG = "GoogleFitScreen";


    public static GoogleApiClient mClient = null;

    @NonNull @Override public UiInfo getUiInfo() {
        return new UiInfo(R.layout.screen_google_fit);
    }

    @Bind(R.id.start_integration) View uiStartIntegration;

    @Inject GoogleFitPresenter googleFitPresenter;

    @Override protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        Injector.inject(this);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleFitPresenter.handleResponse(requestCode, resultCode, data);
    }

    @OnClick(R.id.start_integration) void onStartIntergrationClick() {
        googleFitPresenter.startIntegrate();
    }

    @Override protected void onResume() {
        super.onResume();
        uiStartIntegration.setEnabled(mClient.isConnected());
    }

}
