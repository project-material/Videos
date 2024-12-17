package com.projectmaterial.videos.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.transition.TransitionManager;

import com.google.android.material.transition.MaterialFadeThrough;
import com.projectmaterial.videos.R;
import com.projectmaterial.videos.adapter.CollectionAdapter;
import com.projectmaterial.videos.bottomsheet.CollectionSortingDialogFragment;
import com.projectmaterial.videos.database.Video;
import com.projectmaterial.videos.fragment.base.BaseFragment;
import com.projectmaterial.videos.recyclerview.GridLayoutItemDecoration;
import com.projectmaterial.videos.recyclerview.LinearLayoutItemDecoration;
import com.projectmaterial.videos.util.LayoutType;
import com.projectmaterial.videos.viewmodel.VideoViewModel;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionsFragment extends BaseFragment {
    
    private static final String PREF_KEY_LAYOUT_TYPE = "layout_type";
    
    private CollectionAdapter adapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private Toolbar toolbar;
    private VideoViewModel viewModel;
    
    private boolean isGridView;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        sharedPreferencesEditor = sharedPreferences.edit();
        isGridView = sharedPreferences.getBoolean(PREF_KEY_LAYOUT_TYPE, false);
    }
    
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_collections, menu);
    }
    
    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_collections, container, false);
        setupTransition(parentView);
        return parentView;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_list_view:
                switchLayout(false);
                return true;
            case R.id.menu_grid_view:
                switchLayout(true);
                return true;
            case R.id.menu_sort:
                showSortingOptionsMenu(new CollectionSortingDialogFragment(adapter));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        menu.findItem(R.id.menu_list_view).setVisible(isGridView);
        menu.findItem(R.id.menu_grid_view).setVisible(!isGridView);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(getDrawable(R.drawable.quantum_ic_more_vert_vd_theme_24));
        toolbar.setTitle(R.string.navigation_title_collections);
        setSupportActionBar(toolbar);
        
        emptyStateView = view.findViewById(R.id.empty_state_view);
        emptyStateView.setIcon(R.drawable.quantum_ic_folder_filled_vd_theme_24);
        emptyStateView.setIconBackground(R.drawable.empty_state_view_collections);
        emptyStateView.setTitle(R.string.empty_state_view_title_collections);
        emptyStateView.setMessage(R.string.empty_state_view_message_collections);
        
        adapter = new CollectionAdapter(requireContext());
        
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        configureLayoutManager(isGridView);
        
        viewModel = new ViewModelProvider(this).get(VideoViewModel.class);
        viewModel.getVideosLiveData().observe(getViewLifecycleOwner(), this::onCollectionListChanged);
    }
    
    private void onCollectionListChanged(@Nullable List<Video> videoList) {
        List<String> collectionList = videoList.stream()
                .map(video -> video.getData().substring(0, video.getData().lastIndexOf(File.separator)))
                .distinct()
                .collect(Collectors.toList());
        
        adapter.setCollectionList(collectionList, videoList);
        manageEmptyStateVisibility(collectionList);
    }
    
    private void configureLayoutManager(boolean isGridView) {
        if (isGridView) {
            removeExistingDecorations();
            recyclerView.addItemDecoration(new GridLayoutItemDecoration(requireContext(), 2));
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
            adapter.setLayoutType(LayoutType.GRID);
        } else {
            removeExistingDecorations();
            recyclerView.addItemDecoration(new LinearLayoutItemDecoration(requireContext()));
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            adapter.setLayoutType(LayoutType.LIST);
        }
    }
    
    private void removeExistingDecorations() {
        if (recyclerView.getItemDecorationCount() > 0) {
            recyclerView.removeItemDecorationAt(0);
        }
    }
    
    private void saveLayoutPreference(boolean isGridView) {
        sharedPreferencesEditor.putBoolean(PREF_KEY_LAYOUT_TYPE, isGridView);
        sharedPreferencesEditor.apply();
    }
    
    private void setupTransitionAnimation() {
        ViewGroup parent = (ViewGroup) recyclerView.getParent();
        
        MaterialFadeThrough fadeThrough = new MaterialFadeThrough();
        fadeThrough.setDuration(250);
        
        TransitionManager.beginDelayedTransition(parent, fadeThrough);
        
        requireActivity().invalidateOptionsMenu();
    }
    
    /*
    private void setupTransitionAnimation() {
        ViewGroup parent = (ViewGroup) recyclerView.getParent();
        
        MaterialFadeThrough fadeThrough = new MaterialFadeThrough();
        fadeThrough.setDuration(250);
        
        fadeThrough.addListener(new TransitionListenerAdapter() {
            @Override
            public void onTransitionEnd(@NonNull Transition transition) {
                requireActivity().invalidateOptionsMenu();
            }

            @Override
            public void onTransitionStart(@NonNull Transition transition) {
                requireActivity().invalidateOptionsMenu();
            }
        });
    
        TransitionManager.beginDelayedTransition(parent, fadeThrough);
    }
    */
    
    private void switchLayout(boolean isGridView) {
        this.isGridView = isGridView;
        configureLayoutManager(isGridView);
        saveLayoutPreference(isGridView);
        setupTransitionAnimation();
    }
}