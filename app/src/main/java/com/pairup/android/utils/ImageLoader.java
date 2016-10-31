package com.pairup.android.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.pairup.android.R;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class ImageLoader {

    private final Context context;

    public ImageLoader(@NonNull Context context) {
        this.context = context;
    }

    public static void load(@NonNull Context context, @NonNull ImageView iv, @Nullable String url) {
        if (TextUtils.isEmpty(url)) {
            Picasso.with(context).load(R.drawable.avatar_placeholder).into(iv);
        } else {
            Picasso.with(context).load(url).placeholder(R.drawable.avatar_placeholder).into(iv);
        }
    }

    @Nullable public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (IllegalStateException e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Nullable public String getCorrectLink(Uri uri) {
        if (uri != null) {
            File file = new File(uri.getPath());
            if (!file.exists()) {
                String realPathFromURI = getRealPathFromURI(uri);
                if (realPathFromURI != null) {
                    file = new File(realPathFromURI);
                    if (file.exists()) {
                        return realPathFromURI;
                    }
                }
            } else {
                return uri.getPath();
            }
        }
        return null;
    }
}
