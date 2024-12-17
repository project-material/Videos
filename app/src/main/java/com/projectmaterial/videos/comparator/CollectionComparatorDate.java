package com.projectmaterial.videos.comparator;

import androidx.annotation.NonNull;
import com.projectmaterial.videos.database.Collection;
import com.projectmaterial.videos.database.Video;
import com.projectmaterial.videos.util.CollectionUtils;
import com.projectmaterial.videos.util.VideoUtils;
import java.util.Comparator;

public class CollectionComparatorDate implements Comparator<String> {
    
    public CollectionComparatorDate() {}
    
    @Override
    public int compare(@NonNull String collection1, @NonNull String collection2) {
        String date1 = CollectionUtils.getLastChangedTime(collection1);
        String date2 = CollectionUtils.getLastChangedTime(collection2);
        return date1.compareToIgnoreCase(date2);
    }
}