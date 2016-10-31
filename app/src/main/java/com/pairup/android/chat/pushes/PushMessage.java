package com.pairup.android.chat.pushes;

/**
 * Created by Ilya Eremin on 10/4/16.
 */

public class PushMessage {
    String text;
    String name;
    String avatarUrl;

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
