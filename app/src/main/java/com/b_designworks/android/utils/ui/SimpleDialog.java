package com.b_designworks.android.utils.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.b_designworks.android.R;
import com.b_designworks.android.utils.network.CommonError;
import com.b_designworks.android.utils.network.RetrofitException;

import rx.functions.Action0;

/**
 * Created by Ilya Eremin on 12.08.2016.
 */
public class SimpleDialog {

    public static void withOkBtn(Context context, String message) {
        withOkBtn(context, null, message);
    }

    private static void withOkBtn(
            @NonNull Context context,
            @Nullable String title, String message) {
        show(context, title, message, context.getString(android.R.string.yes), null, null, null);
    }

    public static void show(
            @NonNull Context context,
            @Nullable String title, String message,
            @NonNull String firstButtonLabel, @Nullable Action0 firstBtnAction,
            @Nullable String secondBtnLabel, @Nullable Action0 secondBtnAction) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (firstBtnAction != null) {
                        firstBtnAction.call();
                    }
                    dialog.dismiss();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    if (secondBtnAction != null) {
                        secondBtnAction.call();
                    }
                    dialog.dismiss();
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(firstButtonLabel, dialogClickListener);
        if (secondBtnLabel != null) {
            builder.setNegativeButton(secondBtnLabel, dialogClickListener);
        }
        builder.show();
    }

    public static void show(Context context, RetrofitException error) {
        CommonError parsedError = error.getErrorBodyAs(CommonError.class);
        if (parsedError != null) {
            withOkBtn(context, parsedError.getMessage());
        } else if (error.getKind() == RetrofitException.Kind.NETWORK) {
            withOkBtn(context, context.getString(R.string.error_network));
        }
    }

    public static void networkProblem(Context context) {
        withOkBtn(context, context.getString(R.string.error_network));
    }
}
