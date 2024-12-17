package com.projectmaterial.videos.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.projectmaterial.videos.database.Video;
import com.projectmaterial.videos.worker.VideoFetchWorker;
import java.util.List;

public class VideoViewModel extends AndroidViewModel {
    
    public VideoViewModel(@NonNull Application application) {
        super(application);
    }
    
    public LiveData<List<Video>> getVideosLiveData() {
        return VideoFetchWorker.getVideosLiveData();
    }
}