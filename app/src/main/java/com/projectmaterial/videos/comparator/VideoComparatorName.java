package com.projectmaterial.videos.comparator;

import androidx.annotation.NonNull;
import com.projectmaterial.videos.database.Video;
import com.projectmaterial.videos.util.VideoUtils;
import java.util.Comparator;

public class VideoComparatorName implements Comparator<Video> {
    
    public VideoComparatorName() {}
    
    @Override
    public int compare(@NonNull Video video1, @NonNull Video video2) {
        String displayName1 = VideoUtils.getDisplayNameWithoutSquareBrackets(video1);
        String displayName2 = VideoUtils.getDisplayNameWithoutSquareBrackets(video2);
        return displayName1.compareToIgnoreCase(displayName2);
    }
}