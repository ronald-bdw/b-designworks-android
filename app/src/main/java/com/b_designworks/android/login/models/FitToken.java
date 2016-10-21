package com.b_designworks.android.login.models;

/**
 * Created by almaziskhakov on 21/10/2016.
 */

public class FitToken {
    private String id;
    private String userId;
    private String token;
    private String source;
    private String refreshToken;
    private String createdAt;
    private String updatedAt;

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getSource() {
        return source;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
