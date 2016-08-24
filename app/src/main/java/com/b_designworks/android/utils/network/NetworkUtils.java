package com.b_designworks.android.utils.network;

import android.support.annotation.NonNull;

import com.b_designworks.android.Api;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ilya Eremin on 16.08.2016.
 */
public class NetworkUtils {

    private static volatile Api instance;

    public static Api getApi(@NonNull OkHttpClient okHttpClient, @NonNull Gson mapper) {
        Api localInstance = instance;
        if (localInstance == null) {
            synchronized (Api.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Retrofit.Builder()
                        .client(okHttpClient)
                        .baseUrl(Api.BASE_URL)
                        .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
//                      .addConverterFactory(EnumConverterFactory.create()) // this converter should be before gson converter
                        .addConverterFactory(GsonConverterFactory.create(mapper))
                        .build()
                        .create(Api.class);
                }
            }
        }
        return localInstance;
    }
}
