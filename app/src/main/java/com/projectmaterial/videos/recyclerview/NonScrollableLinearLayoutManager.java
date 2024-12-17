package com.projectmaterial.videos.recyclerview;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;

public class NonScrollableLinearLayoutManager extends LinearLayoutManager {

    public NonScrollableLinearLayoutManager(Context context) {
        super(context);
    }

    public NonScrollableLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
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