package com.pairup.android.utils.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pairup.android.Api;
import com.google.gson.Gson;
import com.pairup.android.utils.storage.UserSettings;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ilya Eremin on 16.08.2016.
 */
public class NetworkUtils {

    private static volatile Api instance;

    public static Api getApi(@NonNull OkHttpClient okHttpClient,
                             @NonNull Gson mapper,
                             @NonNull UserSettings userSettings,
                             @NonNull Context context) {
        Api localInstance = instance;
        if (localInstance == null) {
            synchronized (Api.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Retrofit.Builder()
                        .client(okHttpClient)
                        .baseUrl(Api.BASE_URL)
                        .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create(userSettings, context))
//                      .addConverterFactory(EnumConverterFactory.create()) // this converter should be before gson converter
                        .addConverterFactory(StringConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(mapper))
                        .build()
                        .create(Api.class);
                }
            }
        }
        return localInstance;
    }
}
