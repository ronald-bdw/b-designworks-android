package com.b_designworks.android.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    public static boolean isEmpty(@Nullable String str) {
        return str == null || str.isEmpty();
    }
}
