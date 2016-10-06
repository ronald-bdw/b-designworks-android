package com.b_designworks.android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.b_designworks.android.utils.di.Injector;
import com.b_designworks.android.utils.storage.UserSettings;

import javax.inject.Inject;

/**
 * Created by Ilya Eremin on 22.08.2016.
 */
public class InitialScreen extends AppCompatActivity {

    @Inject UserSettings userSettings;

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
