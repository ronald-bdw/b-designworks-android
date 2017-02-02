package com.pairup.android.sync.models;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by Ilya Eremin on 9/26/16.
 */

public enum Provider {
    @SerializedName("Fitbit")FITBIT("Fitbit"),
    @SerializedName("Googlefit")GOOGLE_FIT("Googlefit"),
    @SerializedName("Healthkit")HEALTH_KIT("Healthkit");

    private String name;

    Provider(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return name.toLowerCase(Locale.ENGLISH);
    }
}