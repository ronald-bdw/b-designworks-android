package com.pairup.android;

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

    private static InitialScreen sActivity;

    private static int SPLASH_DURATION;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_splash);

        sActivity = this;

        Injector.inject(sActivity);

        SPLASH_DURATION = 500;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sActivity!=null) {
                    if (userSettings.userHasToken()) {
                        Navigator.chat(sActivity);
                    } else {
                        Navigator.welcome(sActivity);
                    }
                    sActivity.finish();
                }
            }
        }, SPLASH_DURATION);
    }

    @Override protected void onPause() {
        super.onPause();
        sActivity = null;
        finish();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        sActivity = null;
    }
}
