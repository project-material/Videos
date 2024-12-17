package com.projectmaterial.videos.comparator;

import androidx.annotation.NonNull;
import com.projectmaterial.videos.database.Video;
import com.projectmaterial.videos.util.VideoUtils;
import java.io.File;
import java.util.Comparator;

public class VideoComparatorSize implements Comparator<Video> {
    
    public VideoComparatorSize() {}
    
    @Override
    public int compare(@NonNull Video video1, @NonNull Video video2) {
        long size1 = getSize(video1);
        long size2 = getSize(video2);
        return Long.compare(size1, size2);
    }
    
    private static long getSize(@NonNull Video video) {
        File file = getVideo(video);
        return file.length();
    }
    
    private static File getVideo(@NonNull Video video) {
        String data = video.getData();
        return new File(data);
    }
}