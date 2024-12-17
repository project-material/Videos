package com.projectmaterial.videos.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

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

public class VideoAdapter extends BaseVideoAdapter<VideoViewHolder> implements BottomSheetUtils.OnFavoriteStateChangeListener {
    
    private Context context;
    private List<Video> videoList;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private SortingCriteria.Video currentSortingCriteria = SortingCriteria.Video.VIDEO_NAME;
    private SortingOrder currentSortingOrder = SortingOrder.ASCENDING;
    private static final String SHARED_PREFS_KEY_ADAPTER = "shared_prefs_video_adapter";
    private static final String SHARED_PREFS_KEY_SORTING_CRITERIA = "shared_prefs_sorting_criteria";
    private static final String SHARED_PREFS_KEY_SORTING_ORDER = "shared_prefs_sorting_order";
    
    public VideoAdapter(@NonNull Context context) {
        setHasStableIds(true);
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFS_KEY_ADAPTER, Context.MODE_PRIVATE);
        this.sharedPreferencesEditor = sharedPreferences.edit();
        this.videoList = new ArrayList<>();
        getSortingPreferences();
    }
    
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(itemView);
    }
    
    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        Video video = videoList.get(position);
        AdapterUtils.setItemDuration(context, holder, video);
        AdapterUtils.setItemSubtitle(holder, video);
        AdapterUtils.setItemThumbnail(context, holder, video);
        AdapterUtils.setItemTitle(holder, video);
        AdapterUtils.setOnItemClickListener(context, holder, video);
        AdapterUtils.setOnItemLongClickListener(context, holder, video, this);
    }
    
    @Override
    public int getItemCount() {
        return videoList.size();
    }
    
    @Override
    public long getItemId(int position) {
        return videoList.get(position).getId();
    }
    
    @Override
    public void onFavoriteStateChanged(Video video) {
        boolean newFavoriteStatus = !video.isFavorite();
        video.setFavorite(newFavoriteStatus);
        video.setFavoriteState(context, newFavoriteStatus);
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
    
    public void setVideoList(List<Video> videos) {
        videoList.clear();
        videoList.addAll(videos);
        SortingUtils.sortVideoList(videoList, currentSortingCriteria, currentSortingOrder);
        notifyDataSetChanged();
    }
    
    @Override
    public void updateSortingCriteria(@NonNull SortingCriteria.Video newSortingCriteria) {
        currentSortingCriteria = newSortingCriteria;
        SortingUtils.sortVideoList(videoList, currentSortingCriteria, currentSortingOrder);
        saveSortingCriteria(newSortingCriteria);
        notifyDataSetChanged();
    }
    
    @Override
    public void updateSortingOrder(@NonNull SortingOrder newSortingOrder) {
        currentSortingOrder = newSortingOrder;
        SortingUtils.sortVideoList(videoList, currentSortingCriteria, currentSortingOrder);
        saveSortingOrder(newSortingOrder);
        notifyDataSetChanged();
    }
}