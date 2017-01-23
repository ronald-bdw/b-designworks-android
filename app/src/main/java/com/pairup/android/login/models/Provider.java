package com.pairup.android.login.models;

/**
 * Created by almaziskhakov on 26/10/2016.
 */

public class Provider {
    private String id;
    private String name;
    private int    priority;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

}
