package com.pairup.android;

import android.app.Application;

import com.flurry.android.FlurryAgent;
import com.pairup.android.utils.AndroidUtils;
import com.pairup.android.utils.Bus;
import com.pairup.android.utils.Rxs;
import com.pairup.android.utils.NetworkUtils;
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
import rx.Subscription;

public class App extends Application {

    @Inject UserInteractor userInteractor;

    private AppComponent appComponent;
    private Subscription unauthorizingSubscription = null;

    @Override
    public void onCreate() {
        super.onCreate();
        // Dagger%COMPONENT_NAME%
        appComponent = DaggerAppComponent.builder()
//             list of modules that are part of this component need to be created here too
//             This also corresponds to the name of your module: %component_name%Module
            .appModule(new AppModule(this))
            .build();

        NetworkUtils.initialize(this);
        AndroidUtils.initialize(this);
        setUpServices();
        Injector.inject(this);
        Bus.subscribe(this);
        startFlurry();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserUnauthorizedEvent event) {
        if (userInteractor.getUser() == null) {
            logoutCauseAuthFromAnotherDevice();
        } else if (unauthorizingSubscription == null) {
            unauthorizingSubscription = userInteractor.requestUserStatus(userInteractor.getPhone())
                .doOnTerminate(() -> unauthorizingSubscription = null)
                .compose(Rxs.doInBackgroundDeliverToUI())
                .subscribe(result -> {
                    if (result.isPhoneRegistered()) {
                        logoutCauseAuthFromAnotherDevice();
                    } else {
                        logoutCauseAccDeleted();
                    }
                }, error -> logoutCauseAuthFromAnotherDevice());
        }
    }

    private void logoutCauseAuthFromAnotherDevice() {
        userInteractor.logout();
        Navigator.welcomeWithError(getApplicationContext(), true);
    }

    private void logoutCauseAccDeleted() {
        userInteractor.logout();
        Navigator.welcomeWithError(getApplicationContext(), false);
    }

    private void startFlurry() {
        if (BuildConfig.FLAVOR.equals("production")) {
            FlurryAgent.init(this, "J3MBP2PXHXFS49K7T6QJ");
        }
    }
}