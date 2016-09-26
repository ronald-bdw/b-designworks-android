package com.b_designworks.android.sync;

/**
 * Created by Ilya Eremin on 9/26/16.
 */

public enum Provider {
    FITBIT("fitbit"), GOOGLE_FIT("googlefit"),;

    String name;

    Provider(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return name;
    }
}
