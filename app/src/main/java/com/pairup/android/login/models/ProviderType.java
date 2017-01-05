package com.pairup.android.login.models;

import android.support.annotation.DrawableRes;

import com.google.gson.annotations.SerializedName;
import com.pairup.android.R;

/**
 * Created by almaziskhakov on 05/01/2017.
 */

public enum ProviderType {
    @SerializedName("HBF")HBF("HBF", R.drawable.hbf_logo, R.drawable.logo_hbf_white),
    @SerializedName("BDW")BDW("BDW", 0, 0);

    private              String name;
    @DrawableRes private int    verifyLogoRes;
    @DrawableRes private int    chatLogoRes;

    ProviderType(String name, @DrawableRes int verifyLogoRes, @DrawableRes int chatLogoRes) {
        this.name = name;
        this.verifyLogoRes = verifyLogoRes;
        this.chatLogoRes = chatLogoRes;
    }

    @DrawableRes public int getVerifyLogoRes() {
        return verifyLogoRes;
    }

    @DrawableRes public int getChatLogoRes() {
        return chatLogoRes;
    }

    public boolean hasVerifyLogo() {
        return verifyLogoRes != 0;
    }

    public boolean hasChatLogo() {
        return chatLogoRes != 0;
    }
}
