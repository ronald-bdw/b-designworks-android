package com.b_designworks.android.login.models;

import com.b_designworks.android.sync.*;

import java.util.List;

/**
 * Created by Ilya Eremin on 9/12/16.
 */
public class User {
    private String            id;
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

    public List<Integration> getIntegrations() {
        return integrations;
    }

    public void setIntegrations(List<Integration> integrations) {
        this.integrations = integrations;
    }
}
