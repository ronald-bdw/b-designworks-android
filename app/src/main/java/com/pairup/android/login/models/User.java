package com.pairup.android.login.models;

import java.util.List;

/**
 * Created by Ilya Eremin on 9/12/16.
 */
public class User {
    private String            id;
    private String            zendesk_id;
    private String            authenticationToken;
    private String            firstName;
    private String            lastName;
    private String            email;
    private String            phoneNumber;
    private Avatar            avatar;
    private List<Integration> integrations;
    private Provider          provider;

    public Avatar getAvatar() {
        return avatar;
    }

    public String getId() {
        return id;
    }

    public String getZendesk_id() {
        return zendesk_id;
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

    public boolean hasHbfProvider() {
        return provider != null && "HBF".equals(provider.getName());
    }

    public List<Integration> getIntegrations() {
        return integrations;
    }

    public void setIntegrations(List<Integration> integrations) {
        this.integrations = integrations;
    }
}
