package com.pairup.android.sync.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Revern on 02.02.2017.
 */

public class Activity {
    @SerializedName("started_at")
    public String startedAt;

    @SerializedName("finished_at")
    public String finishedAt;

    @SerializedName("steps_count")
    public int stepsCount;

    @SerializedName("source")
    public String source;

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public void setFinishedAt(String finishedAt) {
        this.finishedAt = finishedAt;
    }

    public void setStepsCount(int stepsCount) {
        this.stepsCount = stepsCount;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public String getFinishedAt() {
        return finishedAt;
    }

    public int getStepsCount() {
        return stepsCount;
    }

    public String getSource() {
        return source;
    }
}
