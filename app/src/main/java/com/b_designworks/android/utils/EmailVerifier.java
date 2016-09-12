package com.b_designworks.android.utils;

import java.util.regex.Pattern;

/**
 * Created by Ilya Eremin on 9/12/16.
 */

public class EmailVerifier {
    public static final Pattern EMAIL_ADDRESS_PATTERN
        = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    );

}
