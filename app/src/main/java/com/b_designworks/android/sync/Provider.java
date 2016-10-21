package com.b_designworks.android.sync;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ilya Eremin on 9/26/16.
 */

public enum Provider {
    @SerializedName("Fitbit") FITBIT("fitbit"),
    @SerializedName("Googlefit") GOOGLE_FIT("googlefit");

    String name;

    Provider(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return name;
    }
}
