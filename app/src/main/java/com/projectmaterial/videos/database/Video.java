package com.projectmaterial.videos.database;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

public class Video {
    
    private String data;
    private String displayName;
    private boolean isFavorite = false;
    private long duration;
    private long id;
    private static final String FAVORITE_KEY_PREFIX = "favorite_";
    
    public Video(long id, String data, String displayName, long duration) {
        this.id = id;
        this.data = data;
        this.displayName = displayName;
        this.duration = duration;
    }
    
    @Override
    public final boolean equals(@Nullable Object obj) {
        return obj instanceof Video && id == ((Video) obj).id;
    }
    
    public String getData() {
        return data;
    }
    
    public String getKey() {
        return data;
    }
    
    public String getDisplayName() {
        int dotIndex = displayName.lastIndexOf('.');
        return displayName.substring(0, dotIndex);
    }
    
    public long getDuration() {
        return duration;
    }
    
    public boolean getFavoriteState(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return isFavorite = sharedPreferences.getBoolean(FAVORITE_KEY_PREFIX + getDisplayName(), false);
    }
    
    public long getId() {
        return id;
    }
    
    public @Nullable Uri getThumbnailUri() {
        
        return ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
            .buildUpon()
            .appendPath("thumbnail")
            .build();
    }
    
    @Override
    public final int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
    
    public boolean isFavorite() {
        return isFavorite;
    }
    
    public void setFavoriteState(Context context, boolean isFavorite) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(FAVORITE_KEY_PREFIX + getDisplayName(), isFavorite).apply();
    }
    
    public void setFavorite(boolean favoriteState) {
        isFavorite = favoriteState;
    }
}