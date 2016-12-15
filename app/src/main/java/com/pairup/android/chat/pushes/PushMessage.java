package com.pairup.android.chat.pushes;

/**
 * Created by Ilya Eremin on 10/4/16.
 */

public class PushMessage {
    private String text;
    private String name;
    private String avatarUrl;

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}
