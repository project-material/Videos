package com.projectmaterial.videos.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.projectmaterial.videos.database.Video;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteVideoViewModel extends ViewModel {

    private final MutableLiveData<Map<Long, Boolean>> favoriteStateMap = new MutableLiveData<>(new HashMap<>());

    // Expose favorite states as LiveData
    public LiveData<Map<Long, Boolean>> getFavoriteStateMap() {
        return favoriteStateMap;
    }

    // Update the favorite state of a video
    public void updateFavoriteState(Video video, boolean isFavorite) {
        Map<Long, Boolean> currentMap = favoriteStateMap.getValue();
        if (currentMap == null) return;

        currentMap.put(video.getId(), isFavorite);
        favoriteStateMap.setValue(currentMap);  // Notify observers
    }

    // Initialize favorite states from a list of videos
    public void setInitialFavoriteStates(List<Video> videoList) {
        Map<Long, Boolean> initialMap = new HashMap<>();
        for (Video video : videoList) {
            initialMap.put(video.getId(), video.isFavorite());
        }
        favoriteStateMap.setValue(initialMap);
    }
}