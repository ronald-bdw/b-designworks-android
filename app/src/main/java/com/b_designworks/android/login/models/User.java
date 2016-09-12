package com.b_designworks.android.login.models;

/**
 * Created by Ilya Eremin on 9/12/16.
 */
public class User {
    private String id;
    private String authenticationToken;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Avatar avatar;

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
}
