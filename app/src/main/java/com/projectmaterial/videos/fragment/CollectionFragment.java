package com.projectmaterial.videos.fragment;

import android.graphics.Insets;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.projectmaterial.videos.R;
import com.projectmaterial.videos.adapter.VideoAdapter;
import com.projectmaterial.videos.database.Video;
import com.projectmaterial.videos.recyclerview.LinearLayoutItemDecoration;
import com.projectmaterial.videos.viewmodel.CollectionViewModel;

import java.util.List;

public class CollectionFragment extends Fragment {
    
    private static final String COLLECTION_KEY = "collection_key";
    
    private String collectionKey;
    private VideoAdapter adapter;
    
    public static CollectionFragment newInstance(@NonNull String collectionKey) {
        CollectionFragment fragment = new CollectionFragment();
        Bundle args = new Bundle();
        args.putString(COLLECTION_KEY, collectionKey);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            collectionKey = getArguments().getString(COLLECTION_KEY);
        }
    }
    
    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_collection, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new VideoAdapter(getContext());
        
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new LinearLayoutItemDecoration(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setOnApplyWindowInsetsListener((v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsets.Type.systemBars());
            v.setPadding(0, 0, 0, systemBars.bottom);
            return WindowInsets.CONSUMED;
        });
        
        CollectionViewModel collectionViewModel = new ViewModelProvider(this).get(CollectionViewModel.class);
        collectionViewModel.getCollectionsLiveData(collectionKey).observe(getViewLifecycleOwner(), this::onVideoListChanged);
    }
    
    private void onVideoListChanged(@Nullable List<Video> videoList) {
        adapter.setVideoList(videoList);
        
        if (videoList == null || videoList.isEmpty()) {
            requireActivity().finish();
        }
    }
}