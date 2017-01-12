package com.pairup.android.utils.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pairup.android.Api;
import com.pairup.android.BuildConfig;
import com.pairup.android.UserInteractor;
import com.pairup.android.chat.ChatPresenter;
import com.pairup.android.login.EnterPhonePresenter;
import com.pairup.android.login.LoginFlowInteractor;
import com.pairup.android.login.SelectProviderPresenter;
import com.pairup.android.login.VerifyPresenter;
import com.pairup.android.profile.EditProfilePresenter;
import com.pairup.android.subscription.SubscriptionPresenter;
import com.pairup.android.sync.FitbitPresenter;
import com.pairup.android.sync.GoogleFitPresenter;
import com.pairup.android.utils.Analytics;
import com.pairup.android.utils.ImageLoader;
import com.pairup.android.utils.network.RxErrorHandlingCallAdapterFactory;
import com.pairup.android.utils.network.StringConverterFactory;
import com.pairup.android.utils.storage.IStorage;
import com.pairup.android.utils.storage.Storage;
import com.pairup.android.utils.storage.UserSettings;

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

    @Provides @Singleton Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    public OkHttpClient provideHttpClient(@NonNull UserSettings userSettings,
                                          @NonNull File cachedDir) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.cache(new Cache(cachedDir, 20 * 1024 * 1024));
        httpClientBuilder.readTimeout(30, TimeUnit.SECONDS);

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
                                                 @NonNull Api api,
                                                 @NonNull NotificationManagerCompat
                                                         notificationManager) {
        return new UserInteractor(storage, userSettings, api, notificationManager);
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
    public VerifyPresenter provideVerifyPresenter(UserInteractor userInteractor,
                                                  LoginFlowInteractor loginFlowInteractor) {
        return new VerifyPresenter(userInteractor, loginFlowInteractor);
    }

    @Provides @Singleton
    public FitbitPresenter provideFitbitPresenter(UserInteractor userInteractor) {
        return new FitbitPresenter(userInteractor);
    }

    @Provides @Singleton
    public GoogleFitPresenter provideGoogleFitPresenter(UserInteractor userInteractor) {
        return new GoogleFitPresenter(userInteractor, mApplication);
    }

    @Provides @Singleton
    public SubscriptionPresenter provideSubscriptionPresenter(Gson gson,
                                                              UserInteractor userInteractor) {
        return new SubscriptionPresenter(gson, userInteractor);
    }

    @Provides @Singleton public LoginFlowInteractor provideLoginFlowInteractor(IStorage storage) {
        return new LoginFlowInteractor(storage);
    }

    @Provides @Singleton public ImageLoader provideImageLoader(Context context) {
        return new ImageLoader(context);
    }

    @Provides @Singleton NotificationManagerCompat provideNotificationManager(Context context) {
        return NotificationManagerCompat.from(context);
    }

    @Provides
    @Singleton
    ChatPresenter provideChatPresenter(UserInteractor userInteractor) {
        return new ChatPresenter(userInteractor);
    }

    @Provides
    @Singleton
    SelectProviderPresenter provideSelectProviderPresenter(UserInteractor userInteractor) {
        return new SelectProviderPresenter(userInteractor);
    }

    @Provides @Singleton Analytics providesAnalytics() {
        return new Analytics();
    }

    @Provides
    @Singleton
    EnterPhonePresenter provideEnterPhonePresenter(UserInteractor userInteractor,
                                                   LoginFlowInteractor loginFlowInteractor,
                                                   Analytics analytics) {
        return new EnterPhonePresenter(userInteractor, loginFlowInteractor, analytics);
    }
}