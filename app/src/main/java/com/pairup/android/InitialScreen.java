package com.pairup.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.storage.UserSettings;

import javax.inject.Inject;

/**
 * Created by Ilya Eremin on 22.08.2016.
 */
public class InitialScreen extends AppCompatActivity {

    @Inject UserSettings userSettings;

    private static final int DELAY_TIME = 3000;
    private Handler mHandler;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()
            && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
            && getIntent().getAction() != null
            && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }
        setContentView(R.layout.screen_splash);

        Injector.inject(this);

        mHandler = new Handler();
        mHandler.postDelayed(() -> {
            if (userSettings.userHasToken()) {
                Navigator.chat(this);
            } else {
                Navigator.welcome(this);
            }
            finish();}
        , DELAY_TIME);
    }

    @Override protected void onStop() {
        super.onStop();
        mHandler.removeCallbacksAndMessages(null);
        finish();
    }
}
