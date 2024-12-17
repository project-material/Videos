package com.projectmaterial.videos.recyclerview;

import android.content.Context;
import android.util.TypedValue;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LinearLayoutItemDecoration extends RecyclerView.ItemDecoration {

    private final int margin16dp;
    private final int spacing8dp;

    public LinearLayoutItemDecoration(@NonNull Context context) {
        this.margin16dp = dpToPx(context, 16);
        this.spacing8dp = dpToPx(context, 8);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION) return;

        int itemCount = parent.getAdapter() != null ? parent.getAdapter().getItemCount() : 0;

        // Apply top margin only for the first item
        if (position == 0) {
            outRect.top = spacing8dp;
        }

        // Apply bottom margin only for the last item
        if (position == itemCount - 1) {
            outRect.bottom = margin16dp;
        } else {
            outRect.bottom = spacing8dp; // Spacing between items
        }

        // Apply equal left and right margins
        outRect.left = margin16dp;
        outRect.right = margin16dp;
    }

    private static int dpToPx(@NonNull Context context, int dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()
        ));
    }
}