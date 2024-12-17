package com.projectmaterial.videos.comparator;

import android.content.Context;
import androidx.annotation.NonNull;
import com.projectmaterial.videos.util.CollectionUtils;
import java.util.Comparator;

public class CollectionComparatorName implements Comparator<String> {
    private Context context;
    
    public CollectionComparatorName(@NonNull Context context) {
        this.context = context;
    }
    
    @Override
    public int compare(@NonNull String collection1, @NonNull String collection2) {
        String name1 = CollectionUtils.getCollectionName(context, collection1);
        String name2 = CollectionUtils.getCollectionName(context, collection2);
        return name1.compareToIgnoreCase(name2);
    }
}