package com.projectmaterial.videos.util;

import android.content.Context;
import com.projectmaterial.videos.comparator.CollectionComparatorDate;
import com.projectmaterial.videos.comparator.CollectionComparatorName;
import com.projectmaterial.videos.comparator.CollectionComparatorVideoCount;
import com.projectmaterial.videos.comparator.VideoComparatorDate;
import com.projectmaterial.videos.comparator.VideoComparatorDuration;
import com.projectmaterial.videos.comparator.VideoComparatorName;
import com.projectmaterial.videos.comparator.VideoComparatorSize;
import com.projectmaterial.videos.database.Video;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SortingUtils {
    
    public static void sortVideoList(List<Video> videos, SortingCriteria.Video criteria, SortingOrder order) {
        VideoComparatorName name = new VideoComparatorName();
        VideoComparatorDuration duration = new VideoComparatorDuration();
        VideoComparatorDate date = new VideoComparatorDate();
        VideoComparatorSize size = new VideoComparatorSize();
        
        switch (criteria) {
            case VIDEO_NAME:
                Collections.sort(videos, name);
                break;
            case VIDEO_DURATION:
                Collections.sort(videos, duration);
                break;
            case VIDEO_DATE:
                Collections.sort(videos, date);
                break;
            case VIDEO_SIZE:
                Collections.sort(videos, size);
                break;
        }
        
        if (order == SortingOrder.DESCENDING) {
            Collections.reverse(videos);
        }
    }
    
    public static void sortCollections(Context context, List<String> collections, Map<String, Integer> collectionMap, SortingCriteria.Collection criteria, SortingOrder order) {
        CollectionComparatorName name = new CollectionComparatorName(context);
        CollectionComparatorDate date = new CollectionComparatorDate();
        CollectionComparatorVideoCount videoCount = new CollectionComparatorVideoCount(collectionMap);
        
    	switch (criteria) {
            case COLLECTION_NAME:
                Collections.sort(collections, name);
                break;
            case COLLECTION_DATE:
                Collections.sort(collections, date);
                break;
            case COLLECTION_VIDEO_COUNT:
                Collections.sort(collections, videoCount);
                break;
        }
        
        if (order.equals(SortingOrder.DESCENDING)) {
            Collections.reverse(collections);
        }
    }
}