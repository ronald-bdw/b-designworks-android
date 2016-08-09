package com.b_designworks.android;

import android.app.Application;

import com.b_designworks.android.utils.AndroidUtils;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import io.smooch.core.Smooch;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidUtils.initialize(this);
        Smooch.init(this, "8maa8895p2nud29ijkrf2ikx1");
        setUpCrashMonitoring();
    }

    protected void setUpCrashMonitoring() {
        Fabric.with(this, new Crashlytics());
        Crashlytics.setString("GIT_SHA", BuildConfig.GIT_SHA);
        Crashlytics.setBool("DEBUG", BuildConfig.DEBUG);
    }

}