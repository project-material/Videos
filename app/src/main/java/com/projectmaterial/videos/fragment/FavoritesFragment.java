package com.projectmaterial.videos.fragment;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.projectmaterial.videos.R;
import com.projectmaterial.videos.adapter.FavoriteVideoAdapter;
import com.projectmaterial.videos.bottomsheet.VideoSortingDialogFragment;
import com.projectmaterial.videos.database.Video;
import com.projectmaterial.videos.fragment.base.BaseFragment;
import com.projectmaterial.videos.recyclerview.LinearLayoutItemDecoration;
import com.projectmaterial.videos.viewmodel.VideoViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class FavoritesFragment extends BaseFragment {
    
    private FavoriteVideoAdapter adapter;
    private Toolbar toolbar;
    private VideoViewModel viewModel;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_favorites, menu);
    }
    
    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_favorites, container, false);
        setupTransition(parentView);
        return parentView;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort:
                showSortingOptionsMenu(new VideoSortingDialogFragment(adapter));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setOverflowIcon(getDrawable(R.drawable.quantum_ic_more_vert_vd_theme_24));
        toolbar.setTitle(R.string.navigation_title_favorites);
        setSupportActionBar(toolbar);
        
        emptyStateView = view.findViewById(R.id.empty_state_view);
        emptyStateView.setIcon(R.drawable.quantum_ic_star_filled_vd_theme_24);
        emptyStateView.setIconBackground(R.drawable.empty_state_view_favorites);
        emptyStateView.setTitle(R.string.empty_state_view_title_favorites);
        emptyStateView.setMessage(R.string.empty_state_view_message_favorites);
        
        adapter = new FavoriteVideoAdapter(requireContext());
        adapter.setOnFavoriteListUpdatedListener(this::manageEmptyStateVisibility);
        
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new LinearLayoutItemDecoration(requireContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        viewModel = new ViewModelProvider(this).get(VideoViewModel.class);
        viewModel.getVideosLiveData().observe(getViewLifecycleOwner(), this::onFavoriteVideoListChanged);
    }
    
    private void onFavoriteVideoListChanged(@Nullable List<Video> videoList) {
        List<Video> favoriteVideoList = videoList.stream()
                .peek(video -> video.setFavorite(video.getFavoriteState(requireContext())))
                .filter(Video::isFavorite)
                .collect(Collectors.toList());

        adapter.setFavoriteVideoList(favoriteVideoList);
        manageEmptyStateVisibility(favoriteVideoList);
    }
}