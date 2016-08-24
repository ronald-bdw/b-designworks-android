package com.b_designworks.android.utils.ui;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Ilya Eremin on 12.08.2016.
 */
public class TextViews {

    public static boolean isEmpty(@NonNull TextView textView) {
        return TextUtils.isEmpty(textView.getText().toString());
    }

    public static boolean isEmptyAfterTrim(@NonNull TextView textView) {
        return TextUtils.getTrimmedLength(textView.getText().toString()) == 0;
    }

    @NonNull public static String textOf(@NonNull TextView tt) {
        return tt.getText().toString();
    }

    public static void moveCarretAtTheEnd(EditText et) {
        et.setSelection(et.getText().length());
    }

}
