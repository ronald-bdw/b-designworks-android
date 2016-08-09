package com.b_designworks.android.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class ImageLoader {

    public static void load(Context context, ImageView iv, String url) {
        Glide.with(context).load(url).into(iv);
    }


}
