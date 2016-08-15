package com.b_designworks.android.utils.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import com.b_designworks.android.R;
import com.b_designworks.android.utils.network.CommonError;
import com.b_designworks.android.utils.network.RetrofitException;

/**
 * Created by Ilya Eremin on 12.08.2016.
 */
public class SimpleOkDialog {
    public static void show(Context context, @StringRes int textRes) {
        show(context, context.getString(textRes));
    }

    public static void show(Context context, String message) {
        show(context, null, message);
    }

    public static void show(
        @NonNull Context context,
        @StringRes int titleRes, @StringRes int messageRes) {
        show(context, context.getString(titleRes), context.getString(messageRes));
    }


    public static void show(
        @NonNull Context context,
        @Nullable String title, String message) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    dialog.dismiss();
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.yes, dialogClickListener)
            .show();
    }

    public static void show(Context context, RetrofitException error) {
        CommonError parsedError = error.getErrorBodyAs(CommonError.class);
        if (parsedError != null) {
            show(context, parsedError.getMessage());
        } else if (error.getKind() == RetrofitException.Kind.NETWORK) {
            show(context, context.getString(R.string.error_network));
        }
    }

    public static void networkProblem(Context context) {
        show(context, context.getString(R.string.error_network));
    }
}
