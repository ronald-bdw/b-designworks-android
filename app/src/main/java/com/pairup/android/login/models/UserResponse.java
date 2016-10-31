package com.pairup.android.login.models;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by Ilya Eremin on 15.08.2016.
 */
public class UserResponse {

    public UserResponse(){}

    public UserResponse(User user) {
        this.user = user;
    }

    private User user;

    public User getUser() {
        return user;
    }

    public String getToken() {
        return user.getAuthenticationToken();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public String getPhoneNumber() {
        return user.getPhoneNumber();
    }

    public String getId() {
        return user.getId();
    }

    public List<Integration> getIntegrations(){
        return user.getIntegrations();
    }

}
