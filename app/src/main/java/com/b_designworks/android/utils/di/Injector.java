package com.b_designworks.android.utils.di;

import android.support.annotation.NonNull;

import com.b_designworks.android.App;
import com.b_designworks.android.sync.GoogleFitScreen;

/**
 * Created by Ilya Eremin on 9/19/16.
 */
public class Injector {

    public static void inject(@NonNull GoogleFitScreen googleFitScreen) {
        ((App) googleFitScreen.getApplicationContext()).getAppComponent().inject(googleFitScreen);
    }
}
