package com.pairup.android.login.models;

import com.google.gson.annotations.SerializedName;
import com.pairup.android.R;

/**
 * Created by almaziskhakov on 05/01/2017.
 */

public enum ProviderType {
    @SerializedName("HBF")HBF("HBF", R.drawable.hbf_logo, R.drawable.logo_hbf_white),
    @SerializedName("BDW")BDW("BDW", 0, 0);

    private final Object[] values;

    ProviderType(Object... values) {
        this.values = values;
    }

    public int getVerifyLogo() {
        return (int) values[1];
    }

    public int getChatLogo() {
        return (int) values[2];
    }
}
