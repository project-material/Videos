package com.projectmaterial.videos.comparator;

import android.content.Context;
import androidx.annotation.NonNull;
import com.projectmaterial.videos.database.Collection;
import java.util.Comparator;
import java.util.Map;

public class CollectionComparatorVideoCount implements Comparator<String> {
    private Map<String, Integer> collectionMap;
    
    public CollectionComparatorVideoCount(@NonNull Map<String, Integer> collectionMap) {
        this.collectionMap = collectionMap;
    }
    
    @Override
    public int compare(@NonNull String collection1, @NonNull String collection2) {
        int count1 = collectionMap.get(collection1);
        int count2 = collectionMap.get(collection2);
        return Integer.compare(count1, count2);
    }
}