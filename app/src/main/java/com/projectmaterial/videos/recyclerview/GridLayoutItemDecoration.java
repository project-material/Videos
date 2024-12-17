package com.projectmaterial.videos.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridLayoutItemDecoration extends RecyclerView.ItemDecoration {

    private final int spanCount;
    private final int spacing8dp;
    private final int spacing16dp;
    private final int spacing24dp;

    public GridLayoutItemDecoration(@NonNull Context context, int spanCount) {
        this.spanCount = spanCount;
        this.spacing8dp = dpToPx(context, 8);
        this.spacing16dp = dpToPx(context, 16);
        this.spacing24dp = dpToPx(context, 24);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION) return;

        int itemCount = parent.getAdapter() != null ? parent.getAdapter().getItemCount() : 0;

        // Calculate column and row positions
        int column = position % spanCount;
        int totalRows = (itemCount + spanCount - 1) / spanCount; // Ceiling division
        int lastRowStartPosition = (totalRows - 1) * spanCount;

        // Horizontal spacing
        outRect.left = column * spacing8dp / spanCount;
        outRect.right = spacing8dp - (column + 1) * spacing8dp / spanCount;

        if (column == 0) { // First column
            outRect.left = spacing24dp;
        } else if (column == spanCount - 1) { // Last column
            outRect.right = spacing24dp;
        }

        // Vertical spacing
        if (position < spanCount) { // Top row
            outRect.top = spacing16dp;
        } else {
            outRect.top = spacing8dp;
        }

        if (position >= lastRowStartPosition) { // Bottom row
            outRect.bottom = spacing24dp;
        } else {
            outRect.bottom = 0;
        }
    }

    private static int dpToPx(@NonNull Context context, int dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()
        ));
    }
}