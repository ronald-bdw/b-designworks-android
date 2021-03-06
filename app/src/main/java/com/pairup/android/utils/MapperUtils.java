package com.pairup.android.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Ilya Eremin on 15.08.2016.
 */
public class MapperUtils {

    private static volatile Gson sInstance;

    public static Gson getInstance() {
        Gson localInstance = sInstance;
        if (localInstance == null) {
            synchronized (Gson.class) {
                localInstance = sInstance;
                if (localInstance == null) {
                    sInstance = localInstance = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create();
                }
            }
        }
        return localInstance;
    }

}
