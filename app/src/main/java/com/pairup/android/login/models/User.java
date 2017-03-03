package com.pairup.android.login.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Ilya Eremin on 9/12/16.
 */
public class User {
    private String            id;
    private String            zendeskId;
    private String            authenticationToken;
    private String            firstName;
    private String            lastName;
    private String            email;
    private String            phoneNumber;
    private Avatar            avatar;
    private List<Integration> integrations;
    private Provider          provider;
    @SerializedName("first_popup_active")
    private boolean           subscriptionExpiringSoon;
    @SerializedName("second_popup_active")
    private boolean           subscriptionExpired;

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public String getId() {
        return id;
    }

    public String getZendeskId() {
        return zendeskId;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Provider getProvider() {
        return provider;
    }

    public boolean hasProvider() {
        return provider != null && provider.isNotSubscriber();
    }

    public List<Integration> getIntegrations() {
        return integrations;
    }

    public void setIntegrations(List<Integration> integrations) {
        this.integrations = integrations;
    }

    public boolean isSubscriptionExpiringSoon() {
        return subscriptionExpiringSoon;
    }

    public boolean isSubscriptionExpired() {
        return subscriptionExpired;
    }

}
