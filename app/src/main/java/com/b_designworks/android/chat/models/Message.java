package com.b_designworks.android.chat.models;

import com.b_designworks.android.chat.ChatElement;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class Message implements ChatElement {
    private String avatar;
    private String message;
    private String senderId;
    private String senderName;
    private long   timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    @Override public Type getType() {
        return Type.MESSAGE;
    }
}
