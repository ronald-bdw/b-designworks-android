package com.pairup.android.login.models;

import com.pairup.android.sync.Provider;

/**
 * Created by almaziskhakov on 20/10/2016.
 */

public class Integration {
    private String   fitnessTokenId;
    private Provider name;
    private boolean  status;

    public String getFitnessTokenId() {
        return fitnessTokenId;
    }

    public void setFitnessTokenId(String fitnessTokenId) {
        this.fitnessTokenId = fitnessTokenId;
    }

    public Provider getProvider() {
        return name;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
