package com.b_designworks.android.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.b_designworks.android.R;
import com.bumptech.glide.Glide;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class ImageLoader {

    public static void load(@NonNull Context context, @NonNull ImageView iv, @Nullable String url) {
        if (TextUtils.isEmpty(url)) {
            Glide.with(context).load(R.drawable.avatar_placeholder).into(iv);
        } else {
            Glide.with(context).load(url).into(iv);
        }
    }


}
