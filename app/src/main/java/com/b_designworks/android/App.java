package com.b_designworks.android;

import android.app.Application;

import com.b_designworks.android.utils.AndroidUtils;
import com.b_designworks.android.utils.di.AppComponent;
import com.b_designworks.android.utils.di.AppModule;
import com.b_designworks.android.utils.di.DaggerAppComponent;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import io.smooch.core.Smooch;

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        // Dagger%COMPONENT_NAME%
        appComponent = DaggerAppComponent.builder()
//             list of modules that are part of this component need to be created here too
            .appModule(new AppModule(this)) // This also corresponds to the name of your module: %component_name%Module
            .build();
        AndroidUtils.initialize(this);
        setUpServices();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    protected void setUpServices() {
        Smooch.init(this, "833kwj0z5wkvt82tpgfbbqn3z");
        setUpCrashMonitoring();
    }

    private void setUpCrashMonitoring() {
        Fabric.with(this, new Crashlytics());
        Crashlytics.setString("GIT_SHA", BuildConfig.GIT_SHA);
        Crashlytics.setBool("DEBUG", BuildConfig.DEBUG);
    }

}