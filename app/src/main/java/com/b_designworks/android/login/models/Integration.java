package com.b_designworks.android.login.models;

/**
 * Created by almaziskhakov on 20/10/2016.
 */

public class Integration {
    private String fitnessTokenId;
    private String name;
    private String status;

    public String getFitnessTokenId() {
        return fitnessTokenId;
    }

    public void setFitnessTokenId(String fitnessTokenId) {
        this.fitnessTokenId = fitnessTokenId;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
