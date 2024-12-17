package com.projectmaterial.videos.util;
import android.content.Context;
import android.content.res.Resources;
import android.icu.text.SimpleDateFormat;
import android.os.Environment;
import androidx.annotation.NonNull;
import com.projectmaterial.videos.database.Video;
import java.io.File;
import java.sql.Date;
import java.util.Locale;
import java.util.Map;
import com.projectmaterial.videos.R;

public class CollectionUtils {
    
    
    public static String getCollectionCount(@NonNull Context context, @NonNull String collection, @NonNull Map<String, Integer> collectionMap) {
        Resources resources = context.getResources();
        int countValue = collectionMap.get(collection).intValue();
    	return resources.getQuantityString(R.plurals.video_count, countValue, countValue);
    }
    
    
    public static String getCollectionPath(@NonNull String collection) {
        String collectionPath = collection + File.separator;
        return collectionPath;
    }
    
   
    public static String getCollectionName(@NonNull Context context, @NonNull String collection) {
        String storagePath = Environment.getExternalStorageDirectory().getPath();
        if (collection.equals(storagePath)) {
            return context.getString(R.string.internal_storage);
        }
        return collection.substring(collection.lastIndexOf(File.separator) + 1);
    }
    
    
    
    public static String getCollectionPath(@NonNull Video video) {
        String data = video.getData();
        if (data != null) {
            String collectionPath = data.substring(0, data.lastIndexOf(File.separator));
            String collectionPathWithLowerCase = collectionPath.toLowerCase();
            File collectionFile = new File(collectionPathWithLowerCase);
            if (collectionFile.exists()) {
                return collectionPath;
            }
            File parentFile = collectionFile.getParentFile();
            if (parentFile != null) {
                File[] parentFiles = parentFile.listFiles();
                if (parentFiles != null) {
                    for (File file : parentFiles) {
                        if (file.isDirectory() && file.getName().equalsIgnoreCase(collectionFile.getName())) {
                            return file.getAbsolutePath();
                        }
                    }
                }
            }
            return collectionPath;
        }
        return null;
    }

    public static String getLastChangedTime(@NonNull String collectionPath) {
        File collection = new File(collectionPath);
        if (collection.exists()) {
            long lastModified = collection.lastModified();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return dateFormat.format(new Date(lastModified));
        }
        return null;
    }
}