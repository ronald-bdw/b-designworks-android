package com.b_designworks.android.chat;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public interface ChatElement {

    public enum Type {
        MESSAGE, DATE_TIME
    }

    Type getType();
}
