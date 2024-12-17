package com.projectmaterial.videos.comparator;

import androidx.annotation.NonNull;
import com.projectmaterial.videos.database.Video;
import com.projectmaterial.videos.util.VideoUtils;
import java.util.Comparator;

public class VideoComparatorDate implements Comparator<Video> {
    
    public VideoComparatorDate() {}
    
    @Override
    public int compare(@NonNull Video video1, @NonNull Video video2) {
        long date1 = VideoUtils.getDate(video1);
        long date2 = VideoUtils.getDate(video2);
        return Long.compare(date1, date2);
    }
}