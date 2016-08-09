package com.b_designworks.android;

import android.app.Application;

import com.b_designworks.android.utils.AndroidUtils;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidUtils.initialize(this);
        setUpCrashMonitoring();
    }

    protected void setUpCrashMonitoring() {
        Fabric.with(this, new Crashlytics());
        Crashlytics.setString("GIT_SHA", BuildConfig.GIT_SHA);
        Crashlytics.setBool("DEBUG", BuildConfig.DEBUG);
    }

}