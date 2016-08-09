package com.b_designworks.android.chat.models;

import com.b_designworks.android.chat.ChatElement;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class DateTime implements ChatElement {

    private String timestamp;

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override public Type getType() {
        return Type.DATE_TIME;
    }
}
