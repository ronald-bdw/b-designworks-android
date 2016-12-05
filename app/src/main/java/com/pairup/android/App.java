package com.pairup.android;

import android.app.Application;

import com.pairup.android.utils.AndroidUtils;
import com.pairup.android.utils.Bus;
import com.pairup.android.utils.Logger;
import com.pairup.android.utils.Rxs;
import com.pairup.android.utils.di.AppComponent;
import com.pairup.android.utils.di.AppModule;
import com.pairup.android.utils.di.DaggerAppComponent;
import com.crashlytics.android.Crashlytics;
import com.pairup.android.utils.di.Injector;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import io.smooch.core.Smooch;

public class App extends Application {

    private AppComponent appComponent;

    @Inject UserInteractor userInteractor;

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
        Injector.inject(this);
        Bus.subscribe(this);
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    protected void setUpServices() {
        Smooch.init(this, "eiw2afikzfabehcj65ilhnp7q");
        setUpCrashMonitoring();
    }

    private void setUpCrashMonitoring() {
        Fabric.with(this, new Crashlytics());
        Crashlytics.setString("GIT_SHA", BuildConfig.GIT_SHA);
        Crashlytics.setBool("DEBUG", BuildConfig.DEBUG);
    }

    private boolean isInUnauthorizingProcess = false;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserUnauthorizedEvent event) {
        if(!isInUnauthorizingProcess) {
            isInUnauthorizingProcess = true;
            userInteractor.requestUserStatus(userInteractor.getPhone())
                    .compose(Rxs.doInBackgroundDeliverToUI())
                    .subscribe(result -> {
                        userInteractor.logout();
                        Navigator.welcomeWithError(getApplicationContext(), result.isPhoneRegistered());
                        isInUnauthorizingProcess = false;
                    }, Logger::e);
        }
    }
}