package com.projectmaterial.videos.util;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;

public class BitmapCache {

    private static final int MAX_MEMORY_KB = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private static final int CACHE_SIZE_KB = MAX_MEMORY_KB / 4;

    private static final LruCache<String, Bitmap> thumbnailCache = new LruCache<>(CACHE_SIZE_KB) {
        @Override
        protected int sizeOf(@NonNull String key, @NonNull Bitmap bitmap) {
            return bitmap.getAllocationByteCount() / 1024;  // size in KB
        }
    };

    private BitmapCache() {}

    public static void clearCache() {
        thumbnailCache.evictAll();
    }

    public static void removeFromCache(@NonNull String key) {
        thumbnailCache.remove(key);
    }

    public static @Nullable Bitmap get(@NonNull String key) {
        Bitmap bitmap = thumbnailCache.get(key);
        return (bitmap != null && !bitmap.isRecycled()) ? bitmap : null;
    }

    public static boolean isCached(@NonNull String key) {
        Bitmap cachedBitmap = thumbnailCache.get(key);
        return cachedBitmap != null && !cachedBitmap.isRecycled();
    }

    public static void put(@NonNull String key, @NonNull Bitmap bitmap) {
        thumbnailCache.put(key, bitmap);
    }
}