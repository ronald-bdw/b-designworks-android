package com.b_designworks.android.login.models;

/**
 * Created by Ilya Eremin on 15.08.2016.
 */
public class RegisterResponse {

    private User user;

    public String getToken() {
        return user.authenticationToken;
    }

    public String getEmail() {
        return user.email;
    }

    public String getId() {
        return user.id;
    }

    public static class User {
        private String id;
        private String authenticationToken;
        private String email;
    }

}
