package com.b_designworks.android.utils.di;

import com.b_designworks.android.sync.GoogleFitScreen;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Ilya Eremin on 9/19/16.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(GoogleFitScreen activity);
}
