package com.pairup.android.login.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by almaziskhakov on 26/10/2016.
 */

public class Provider {
    private                                         String  id;
    private                                         String  name;
    private                                         int     priority;
    private                                         boolean subscriber;
    @SerializedName("first_popup_message") private  String  loginSubscriptionExpiringMessage;
    @SerializedName("second_popup_message") private String  chatSubscriptionExpiredMessage;

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

    public String getLoginSubscriptionExpiringMessage() {
        return loginSubscriptionExpiringMessage;
    }

    public String getChatSubscriptionExpiredMessage() {
        return chatSubscriptionExpiredMessage;
    }

}
