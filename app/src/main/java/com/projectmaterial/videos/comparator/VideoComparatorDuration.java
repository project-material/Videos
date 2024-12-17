package com.projectmaterial.videos.comparator;

import androidx.annotation.NonNull;
import com.projectmaterial.videos.database.Video;
import java.util.Comparator;

public class VideoComparatorDuration implements Comparator<Video> {
    
    public VideoComparatorDuration() {}
    
    @Override
    public int compare(@NonNull Video video1, @NonNull Video video2) {
        long duration1 = video1.getDuration();
        long duration2 = video2.getDuration();
        return Long.compare(duration1, duration2);
    }
}