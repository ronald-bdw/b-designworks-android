package com.b_designworks.android.utils.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.b_designworks.android.Api;
import com.b_designworks.android.BuildConfig;
import com.b_designworks.android.UserInteractor;
import com.b_designworks.android.login.VerifyPresenter;
import com.b_designworks.android.profile.EditProfilePresenter;
import com.b_designworks.android.sync.GoogleFitInteractor;
import com.b_designworks.android.utils.network.RxErrorHandlingCallAdapterFactory;
import com.b_designworks.android.utils.network.StringConverterFactory;
import com.b_designworks.android.utils.storage.IStorage;
import com.b_designworks.android.utils.storage.Storage;
import com.b_designworks.android.utils.storage.UserSettings;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    Context providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    GoogleFitInteractor provideGoogleFitInteractor(
        @NonNull Api api) {
        return new GoogleFitInteractor(api);
    }

    @Provides
    @Singleton
    public OkHttpClient provideHttpClient(@NonNull UserSettings userSettings,
                                          @NonNull File cachedDir) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.cache(new Cache(cachedDir, 20 * 1024 * 1024))
            .writeTimeout(60, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(interceptor);
        }
        httpClientBuilder.addInterceptor(chain -> {
            if (userSettings.userHasToken()) {
                Request request = chain.request().newBuilder()
                    .addHeader("X-User-Token", userSettings.getToken())
                    .addHeader("X-User-Phone-Number", userSettings.getPhone())
                    .build();
                return chain.proceed(request);
            }
            return chain.proceed(chain.request());
        });
        return httpClientBuilder.build();
    }

    @Provides
    @Singleton
    public UserSettings providesUserSettings(@NonNull IStorage storage) {
        return new UserSettings(storage);
    }

    @Provides
    @Singleton
    public UserInteractor providesUserInteractor(@NonNull IStorage storage,
                                                 @NonNull UserSettings userSettings,
                                                 @NonNull Api api) {
        return new UserInteractor(storage, userSettings, api);
    }

    @Provides @Singleton
    public IStorage providesStorage(@NonNull SharedPreferences sp, @NonNull Gson mapper) {
        return new Storage(sp, mapper);
    }

    @Provides @Singleton public SharedPreferences providesSharedPrefs(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides @Singleton public Gson getMapper() {
        return new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
    }

    @Provides @Singleton public File getCacheDir(@NonNull Context context) {
        final File external = context.getExternalCacheDir();
        return external != null ? external : context.getCacheDir();
    }

    @Provides @Singleton
    public Api providesApi(@NonNull OkHttpClient httpClient, @NonNull Gson mapper) {
        return new Retrofit.Builder()
            .client(httpClient)
            .baseUrl(Api.BASE_URL)
            .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
            .addConverterFactory(StringConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(mapper))
            .build()
            .create(Api.class);
    }

    @Provides @Singleton
    public EditProfilePresenter provideEditProfilePresenter(UserInteractor userInteractor) {
        return new EditProfilePresenter(userInteractor);
    }

    @Provides @Singleton
    public VerifyPresenter provideVerifyPresenter(UserInteractor userInteractor) {
        return new VerifyPresenter(userInteractor);
    }

}