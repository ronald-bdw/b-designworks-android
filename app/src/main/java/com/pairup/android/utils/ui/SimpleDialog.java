package com.pairup.android.utils.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import com.pairup.android.R;
import com.pairup.android.utils.network.models.CommonError;
import com.pairup.android.utils.network.models.RetrofitException;

import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by Ilya Eremin on 12.08.2016.
 */
public class SimpleDialog {

    public static void withOkBtn(@NonNull Context context, @Nullable String message) {
        withOkBtn(context, null, message);
    }

    private static void withOkBtn(
        @NonNull Context context,
        @Nullable String title, @Nullable String message) {
        show(context, title, message, context.getString(android.R.string.yes), null, null, null);
    }

    public static void show(
        @NonNull Context context,
        @Nullable String title, @Nullable String message,
        @NonNull String buttonLabel, @Nullable Action0 action) {
        show(context, title, message, buttonLabel, action, null, null);
    }

    public static void show(
        @NonNull Context context,
        @Nullable String title, @Nullable String message,
        @NonNull String buttonLabel, @Nullable Action0 action, boolean cancelable) {
        show(context, title, message, buttonLabel, action, null, null, cancelable);
    }

    public static void withOkBtn(@NonNull Context context, @StringRes int labelRes,
                                 @StringRes int descriptionRes, @NonNull Action0 onOkClick) {
        show(context, context.getString(labelRes), context.getString(descriptionRes),
            context.getString(R.string.ok),
            onOkClick);
    }

    public static void show(
        @NonNull Context context,
        @Nullable String title, @Nullable String message,
        @NonNull String firstButtonLabel, @Nullable Action0 firstBtnAction,
        @Nullable String secondBtnLabel, @Nullable Action0 secondBtnAction) {
        show(context, title, message, firstButtonLabel, firstBtnAction,
            secondBtnLabel, secondBtnAction, true);
    }

    //CHECKSTYLE:OFF: checkstyle:parameternumbercheck
    public static void show(
        @NonNull Context context,
        @Nullable String title, @Nullable String message,
        @NonNull String firstButtonLabel, @Nullable Action0 firstBtnAction,
        @Nullable String secondBtnLabel, @Nullable Action0 secondBtnAction, boolean cancelable) {
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
                default:
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
            .setTitle(title)
            .setCancelable(cancelable)
            .setMessage(message)
            .setPositiveButton(firstButtonLabel, dialogClickListener);
        if (secondBtnLabel != null) {
            builder.setNegativeButton(secondBtnLabel, dialogClickListener);
        }
        builder.show();
    }
    //CHECKSTYLE:ON: checkstyle:parameternumbercheck

    public static void show(@NonNull Context context, @NonNull RetrofitException error) {
        CommonError parsedError = error.getErrorBodyAs(CommonError.class);
        if (parsedError != null) {
            withOkBtn(context, parsedError.getMessage());
        } else if (error.getKind() == RetrofitException.Kind.NETWORK) {
            withOkBtn(context, context.getString(R.string.error_network));
        }
    }

    public static void networkProblem(@NonNull Context context) {
        withOkBtn(context, context.getString(R.string.error_network));
    }

    public static void showList(@NonNull Context context,
                                @Nullable String title,
                                @NonNull String[] items,
                                @NonNull Action1<Integer> firstBtnAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
            .setTitle(title)
            .setItems(items, (dialog, id) -> firstBtnAction.call(id));
        builder.show();
    }
}