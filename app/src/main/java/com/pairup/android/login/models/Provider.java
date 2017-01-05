package com.pairup.android.login.models;

/**
 * Created by almaziskhakov on 26/10/2016.
 */

public class Provider {
    private String       id;
    private ProviderType name;

    public String getId() {
        return id;
    }

    public ProviderType getProviderType() {
        return name;
    }
}
