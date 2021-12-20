package com.bitrefactor.igniterbiu.views;

import static com.bitrefactor.igniterbiu.utils.O2OKt.dip2px;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MarginDecoration extends RecyclerView.ItemDecoration {

    private final int margin;
    private final int raw;

    public MarginDecoration(Context context, Float dip) {
        margin = dip2px(context, dip);
        this.raw=2;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) % raw == 0) {
            outRect.set(margin, margin, margin, 0);
        } else {
            outRect.set(0, margin, margin, 0);
        }

    }

}