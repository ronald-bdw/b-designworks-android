package com.b_designworks.android.utils;

import android.support.annotation.NonNull;

import java.util.Collection;

/**
 * Created by Ilya Eremin on 16.08.2016.
 */
public class Strings {

    @NonNull public static String listToString(@NonNull Collection<String> strings) {
        String result = "";
        for (String string : strings) {
            result += string + ", ";
        }
        if (!result.isEmpty()) {
            return result.substring(0, result.length() - 2);
        } else {
            return result;
        }
    }

}
