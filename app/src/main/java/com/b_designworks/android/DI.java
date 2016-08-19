package com.b_designworks.android;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.b_designworks.android.utils.network.NetworkUtils;
import com.b_designworks.android.utils.storage.IStorage;
import com.b_designworks.android.utils.storage.Storage;
import com.crashlytics.android.Crashlytics;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Ilya Eremin on 14.03.2016.
 */
public class DI {

    @SuppressWarnings("StaticFieldLeak") private static volatile DI instance;

    public static DI initialize(@NonNull Context context) {
        DI localInstance = instance;
        if (localInstance == null) {
            synchronized (DI.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DI(context);
                }
            }
        }
        return localInstance;
    }

    private final Context context;

    private DI(Context context) {
        this.context = context;
    }

    @NonNull public static DI getInstance() {
        if (instance == null) {
            Crashlytics.log("DI instance cannot be null here. Something went wrong!");
            throw new IllegalStateException("DI instance cannot be null here. Something went wrong!");
        }
        return instance;
    }

    private Context getContext() {
        return context;
    }

    public @NonNull File getCacheDir() {
        final File external = getContext().getExternalCacheDir();
        return external != null ? external : getContext().getCacheDir();
    }


    public @NonNull Api getApi() {
        return NetworkUtils.getApi(getHttpClient(), getMapper());
    }

    private OkHttpClient okHttpClient;

    private OkHttpClient getHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.cache(new Cache(getCacheDir(), 20 * 1024 * 1024));
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClientBuilder.addInterceptor(interceptor);
            }
            httpClientBuilder.addInterceptor(chain -> {
                UserManager userInteractor = getUserManager();
                if (!TextUtils.isEmpty(userInteractor.getToken())) {
                    Request request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + userInteractor.getToken())
                        .build();
                    return chain.proceed(request);
                }
                return chain.proceed(chain.request());
            });
            okHttpClient = httpClientBuilder.build();
        }
        return okHttpClient;

    private IStorage getStorage() {
        return Storage.getInstance(PreferenceManager.getDefaultSharedPreferences(getContext()), getMapper());
    }

    private Gson gson;

    @NonNull public Gson getMapper() {
        if (gson == null) {
            gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        }
        return gson;

    }
}
