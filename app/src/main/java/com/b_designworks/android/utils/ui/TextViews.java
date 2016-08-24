package com.b_designworks.android.utils.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    public static boolean isSameText(@Nullable TextView textView, @Nullable TextView textView2) {
        String text1 = textOf(textView);
        String text2 = textOf(textView2);
        return text1 != null && text2 != null && text1.equals(text2);
    }

    @NonNull public static String textOf(@NonNull TextView tt) {
        return tt.getText() == "" ? null : tt.getText().toString();
    }

    @Nullable public static String trimmedTextOf(@NonNull TextView tt) {
        String text = textOf(tt);
        return text != null ? text.trim() : null;
    }

    public static int symbolsIn(TextView tv) {
        String text = textOf(tv);
        return text == null ? 0 : text.length();
    }

    public static void moveCarretAtTheEnd(EditText et) {
        et.setSelection(et.getText().length());
    }

}
