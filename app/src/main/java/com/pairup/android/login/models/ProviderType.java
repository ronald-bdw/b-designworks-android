package com.pairup.android.login.models;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Created by almaziskhakov on 05/01/2017.
 */

public enum ProviderType {
    @SerializedName("HBF")HBF("HBF"),
    @SerializedName("BDW")BDW("BDW");

    private String name;

    ProviderType(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return name.toUpperCase(Locale.ENGLISH);
    }
}
