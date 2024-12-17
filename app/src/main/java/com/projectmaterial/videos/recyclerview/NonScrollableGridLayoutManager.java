package com.projectmaterial.videos.recyclerview;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;

public class NonScrollableGridLayoutManager extends GridLayoutManager {

    public NonScrollableGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public NonScrollableGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
    
    @Override
    public boolean canScrollHorizontally() {
        return false;
    }
}