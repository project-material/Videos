package com.projectmaterial.videos.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.projectmaterial.videos.R;
import com.projectmaterial.videos.adapter.base.BaseVideoAdapter;
import com.projectmaterial.videos.bottomsheet.BottomSheetUtils;
import com.projectmaterial.videos.database.Video;
import com.projectmaterial.videos.util.AdapterUtils;
import com.projectmaterial.videos.util.SortingCriteria;
import com.projectmaterial.videos.util.SortingOrder;
import com.projectmaterial.videos.util.SortingUtils;
import com.projectmaterial.videos.viewholder.VideoViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FavoriteVideoAdapter extends BaseVideoAdapter<VideoViewHolder> implements BottomSheetUtils.OnFavoriteStateChangeListener {
    
    private static final String SHARED_PREFS_KEY_ADAPTER = "shared_prefs_favorite_video_adapter";
    private static final String SHARED_PREFS_KEY_SORTING_CRITERIA = "shared_prefs_sorting_criteria";
    private static final String SHARED_PREFS_KEY_SORTING_ORDER = "shared_prefs_sorting_order";
    
    private Context context;
    private List<Video> favoriteVideoList;
    private OnFavoriteListUpdatedListener favoriteStateChangeListener;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private SortingCriteria.Video currentSortingCriteria = SortingCriteria.Video.VIDEO_NAME;
    private SortingOrder currentSortingOrder = SortingOrder.ASCENDING;
    
    public FavoriteVideoAdapter(@NonNull Context context) {
        setHasStableIds(true);
        this.context = context;
        this.favoriteVideoList = new ArrayList<>();
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFS_KEY_ADAPTER, Context.MODE_PRIVATE);
        this.sharedPreferencesEditor = sharedPreferences.edit();
        getSortingPreferences();
    }
    
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(itemView);
    }
    
    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = favoriteVideoList.get(position);
        AdapterUtils.setItemDuration(context, holder, video);
        AdapterUtils.setItemSubtitle(holder, video);
        AdapterUtils.setItemThumbnail(context, holder, video);
        AdapterUtils.setItemTitle(holder, video);
        AdapterUtils.setOnItemClickListener(context, holder, video);
        AdapterUtils.setOnItemLongClickListener(context, holder, video, this);
    }
    
    @Override
    public int getItemCount() {
        return favoriteVideoList.size();
    }
    
    @Override
    public long getItemId(int position) {
        return favoriteVideoList.get(position).getId();
    }
    
    @Override
    public void onFavoriteStateChanged(Video video) {
        boolean newFavoriteState = !video.isFavorite();
        video.setFavorite(newFavoriteState);
        video.setFavoriteState(context, newFavoriteState);

        List<Video> newFavoriteVideoList = updateFavoriteVideoList();
        setFavoriteVideoList(newFavoriteVideoList);

        // Notify the fragment to manage empty state visibility
        if (favoriteStateChangeListener != null) {
            favoriteStateChangeListener.onFavoriteListUpdated(newFavoriteVideoList);
        }
    }
    
    @Override
    public SortingCriteria.Video getSortingCriteria() {
        return currentSortingCriteria;
    }
    
    @Override
    public SortingOrder getSortingOrder() {
        return currentSortingOrder;
    }
    
    private void getSortingPreferences() {
        String sortingCriteria = sharedPreferences.getString(SHARED_PREFS_KEY_SORTING_CRITERIA, null);
        String sortingOrder = sharedPreferences.getString(SHARED_PREFS_KEY_SORTING_ORDER, null);
        if (sortingCriteria != null) currentSortingCriteria = SortingCriteria.Video.valueOf(sortingCriteria);
        if (sortingOrder != null) currentSortingOrder = SortingOrder.valueOf(sortingOrder);
    }
    
    private void saveSortingCriteria(@NonNull SortingCriteria.Video newSortingCriteria) {
        sharedPreferencesEditor.putString(SHARED_PREFS_KEY_SORTING_CRITERIA, newSortingCriteria.name());
        sharedPreferencesEditor.apply();
    }
    
    private void saveSortingOrder(@NonNull SortingOrder newSortingOrder) {
        sharedPreferencesEditor.putString(SHARED_PREFS_KEY_SORTING_ORDER, newSortingOrder.name());
        sharedPreferencesEditor.apply();
    }
    
    public void setFavoriteVideoList(List<Video> videoList) {
        favoriteVideoList.clear();
        favoriteVideoList.addAll(videoList);
        SortingUtils.sortVideoList(favoriteVideoList, currentSortingCriteria, currentSortingOrder);
        notifyDataSetChanged();
    }
    
    public void setOnFavoriteListUpdatedListener(OnFavoriteListUpdatedListener listener) {
        this.favoriteStateChangeListener = listener;
    }
    
    private List<Video> updateFavoriteVideoList() {
        List<Video> updatedFavoriteVideoList = favoriteVideoList.stream()
                .filter(Video::isFavorite)
                .collect(Collectors.toList());
        return updatedFavoriteVideoList;
    }
    
    @Override
    public void updateSortingCriteria(@NonNull SortingCriteria.Video newSortingCriteria) {
        currentSortingCriteria = newSortingCriteria;
        SortingUtils.sortVideoList(favoriteVideoList, currentSortingCriteria, currentSortingOrder);
        saveSortingCriteria(newSortingCriteria);
        notifyDataSetChanged();
    }
    
    @Override
    public void updateSortingOrder(@NonNull SortingOrder newSortingOrder) {
        currentSortingOrder = newSortingOrder;
        SortingUtils.sortVideoList(favoriteVideoList, currentSortingCriteria, currentSortingOrder);
        saveSortingOrder(newSortingOrder);
        notifyDataSetChanged();
    }
    
    public interface OnFavoriteListUpdatedListener {
        void onFavoriteListUpdated(@Nullable List<Video> updatedFavoriteList);
    }
}