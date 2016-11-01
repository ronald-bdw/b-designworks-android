package com.pairup.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.pairup.android.utils.di.Injector;
import com.pairup.android.utils.network.RxErrorHandlingCallAdapterFactory;
import com.pairup.android.utils.storage.UserSettings;

import javax.inject.Inject;

/**
 * Created by Ilya Eremin on 22.08.2016.
 */
public class InitialScreen extends AppCompatActivity {

    @Inject UserSettings                      userSettings;
    @Inject RxErrorHandlingCallAdapterFactory rxErrorHandlingCallAdapterFactory;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.inject(this);
        if (userSettings.userHasToken()) {
            Navigator.chat(this);
        } else {
            Navigator.welcome(this);
        }
        finish();
    }
}
