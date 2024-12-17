package com.projectmaterial.videos.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private final int spacing;
    private final GridLayoutManager layoutManager;
    
    public GridSpacingItemDecoration(Context context, int dp, GridLayoutManager layoutManager) {
        this.spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        this.layoutManager = layoutManager;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int spanSize = layoutManager.getSpanSizeLookup().getSpanSize(position);
        int spanIndex = layoutManager.getSpanSizeLookup().getSpanIndex(position, layoutManager.getSpanCount());
        int spanCount = layoutManager.getSpanCount();
        int spanSizeTotal = spanSize + spanIndex;
        
        outRect.left = (spanIndex == 0) ? 0 : spacing / 2;
        outRect.right = (spanSizeTotal % spanCount == 0) ? 0 : spacing / 2;
        
        if (position < (parent.getAdapter().getItemCount() - spanCount)) {
            outRect.bottom = spacing;
        } else {
            outRect.bottom = 0;
        }
    }
}