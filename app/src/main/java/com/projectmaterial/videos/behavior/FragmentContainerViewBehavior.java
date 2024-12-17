package com.projectmaterial.videos.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentContainerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentContainerViewBehavior extends CoordinatorLayout.Behavior<FragmentContainerView> {
    
    public FragmentContainerViewBehavior() {
        super();
    }
    
    public FragmentContainerViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull FragmentContainerView child, @NonNull View dependency) {
        return dependency instanceof BottomNavigationView;
    }
    
    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull FragmentContainerView child, @NonNull View dependency) {
        if (dependency instanceof BottomNavigationView) {
            int bottomNavHeight = dependency.getHeight();
            child.setClipChildren(false);
            child.setClipToPadding(false);
            child.setPadding(0, 0, 0, bottomNavHeight);
            return true;
        }
        return false;
    }
}