package com.pairup.android.sync.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Revern on 02.02.2017.
 */

public class ActivityToSend {

    @SerializedName("activities")
    public List<Activity> activities;

    public ActivityToSend(List<Activity> activities){
        this.activities = activities;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}
