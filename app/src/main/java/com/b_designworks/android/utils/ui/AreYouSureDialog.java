package com.b_designworks.android.utils.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import rx.functions.Action0;

/**
 * Created by Ilya Eremin on 22.08.2016.
 */
public class AreYouSureDialog {

    public static void show(@NonNull Context context,
                            @StringRes int textRes,
                            @Nullable Action0 okClickListener) {
        show(context, context.getString(textRes), okClickListener);
    }

    public static void show(@NonNull Context context,
                            String message,
                            @Nullable Action0 okClickListener) {
        show(context, null, message, okClickListener);
    }

    public static void show(@NonNull Context context,
                            @StringRes int titleRes,
                            @StringRes int messageRes, Action0 okClickListener) {
        show(context, context.getString(titleRes), context.getString(messageRes), okClickListener);
    }


    public static void show(
        @NonNull Context context,
        @Nullable String title, String message,
        @Nullable Action0 okClickListener) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (okClickListener != null) {
                        okClickListener.call();
                    }
                    dialog.dismiss();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.yes, dialogClickListener)
            .setNegativeButton(android.R.string.no, dialogClickListener).show();
    }

}