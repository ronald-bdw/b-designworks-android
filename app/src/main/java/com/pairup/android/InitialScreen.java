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

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_splash);

        InitialScreen activity = this;

        Injector.inject(activity);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userSettings.userHasToken()) {
                    Navigator.chat(activity);
                } else {
                    Navigator.welcome(activity);
                }
                finish();
            }
        }, 500);
    }
}
