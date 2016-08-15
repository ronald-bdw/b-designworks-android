package com.b_designworks.android;

import android.content.Context;
import android.support.annotation.NonNull;

import com.b_designworks.android.utils.network.RxErrorHandlingCallAdapterFactory;
import com.crashlytics.android.Crashlytics;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    private Api api;

    public @NonNull Api getApi() {
        if (api == null) {
            api = new Retrofit.Builder()
                .client(getHttpClient())
                .baseUrl(Api.BASE_URL)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
//            .addConverterFactory(EnumConverterFactory.create()) // this converter should be before gson converter
                .addConverterFactory(GsonConverterFactory.create(getMapper()))
                .build()
                .create(Api.class);
        }
        return api;
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
            okHttpClient = httpClientBuilder.build();
        }
        return okHttpClient;

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
