package com.pairup.android.utils.ui;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pairup.android.utils.AndroidUtils;

/**
 * Created by Ilya Eremin on 04.08.2016.
 */
public class SpaceDecorator extends RecyclerView.ItemDecoration {

    private final int top;
    private final int left;
    private final int right;
    private final int bottom;

    /**
     * @param top    in dp
     * @param left   in dp
     * @param right  in dp
     * @param bottom in dp
     */
    public SpaceDecorator(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.left = AndroidUtils.dp(left);
        outRect.right = AndroidUtils.dp(right);
        outRect.top = AndroidUtils.dp(top);
        outRect.bottom = AndroidUtils.dp(bottom);
    }
}
