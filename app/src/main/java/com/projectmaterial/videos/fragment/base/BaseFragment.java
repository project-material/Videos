package com.projectmaterial.videos.fragment.base;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.transition.MaterialFadeThrough;
import com.google.android.material.transition.MaterialSharedAxis;
import com.projectmaterial.videos.widget.EmptyStateView;

import java.util.List;

public abstract class BaseFragment extends Fragment {













    
    @Override
    @SuppressWarnings("deprecation")
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return false;
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        
    }
    
    @Nullable
    public final Drawable getDrawable(@DrawableRes int id) {
        return ContextCompat.getDrawable(requireContext(), id);
    }
    
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void showSortingOptionsMenu(@Nullable BottomSheetDialogFragment dialogFragment) {
        dialogFragment.show(getChildFragmentManager(), null);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    protected EmptyStateView emptyStateView;
    protected RecyclerView recyclerView;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setupTransition(container);
        return null;
    }
    
    private void setupTransition(@Nullable ViewGroup container) {
    	MaterialSharedAxis sharedAxis = new MaterialSharedAxis(MaterialSharedAxis.Y, true);
        setEnterTransition(sharedAxis);
        setExitTransition(sharedAxis);
        ViewCompat.setTransitionName(container, "shared_axis");
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postponeEnterTransition();
        view.post(this::startPostponedEnterTransition);
    }
    
    
    public void setupTransition(@Nullable View rootView) {
        MaterialSharedAxis sharedAxis = new MaterialSharedAxis(MaterialSharedAxis.Y, true);
        setEnterTransition(sharedAxis);
        setExitTransition(sharedAxis);
    	rootView.setTransitionName("transition");
    }
    
    public void manageEmptyStateVisibility(@Nullable List<?> objectList) {
        ViewGroup parentView = (ViewGroup) recyclerView.getParent();
        
        if (parentView != null) {
            TransitionManager.beginDelayedTransition(parentView, new MaterialFadeThrough());
        }
        
        emptyStateView.setVisibility(isEmptyList(objectList) ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isEmptyList(objectList) ? View.GONE : View.VISIBLE);
    }
    
    public void invalidateOptionsMenu() {
        requireActivity().invalidateOptionsMenu();
    }
    
    private boolean isEmptyList(@Nullable List<?> objectList) {
        return (objectList == null || objectList.isEmpty());
    }
}