package com.b_designworks.android.utils.di;

import android.app.Application;

import com.b_designworks.android.sync.GoogleFitInteractor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Ilya Eremin on 9/19/16.
 */
@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    GoogleFitInteractor provideGoogleFitInteractor(){

    }

}
