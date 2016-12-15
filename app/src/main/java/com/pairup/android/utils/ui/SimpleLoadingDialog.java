package com.pairup.android.utils.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.pairup.android.R;

import rx.functions.Action0;

/**
 * Created by Ilya Eremin on 9/7/16.
 */

public class SimpleLoadingDialog {

    public static ProgressDialog show(@NonNull Context context,
                                      @NonNull String message,
                                      @NonNull Action0 doOnCancel) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getString(R.string.loading));
        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
            context.getString(R.string.cancel), (dialog, which) -> {
                dialog.dismiss();
            });
        progressDialog.setOnCancelListener(dialog -> doOnCancel.call());
        progressDialog.setOnDismissListener(dialog -> doOnCancel.call());
        progressDialog.show();
        return progressDialog;
    }

}
