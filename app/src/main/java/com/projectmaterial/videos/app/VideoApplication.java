
package com.projectmaterial.videos.app;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.projectmaterial.videos.worker.VideoFetchWorker;

public class VideoApplication extends Application {
    
    private Activity currentActivity;
    private static VideoApplication instance;
    
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ContentObserver contentObserver = new VideoContentObserver(new Handler(Looper.getMainLooper()));
        ContentResolver contentResolver = getContentResolver();
        contentResolver.registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, true, contentObserver);
        fetchContent();
    }
    
    public void fetchContent() {
        OneTimeWorkRequest fetchContentWork = new OneTimeWorkRequest.Builder(VideoFetchWorker.class).build();
        WorkManager.getInstance(this).enqueue(fetchContentWork);
    }
    
    public static VideoApplication getInstance() {
    	return instance;
    }
    
    public void setCurrentActivity(@NonNull Activity activity) {
    	this.currentActivity = activity;
    }
    
    public Activity getCurrentActivity() {
        return currentActivity;
    }
    
    private static class VideoContentObserver extends ContentObserver {
        
        public VideoContentObserver(Handler handler) {
            super(handler);
        }
        
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            VideoApplication.getInstance().fetchContent();
        }
    }
    
    
    
    
    
    
    
    /*
    
    
    private final ContentObserver contentObserver = new VideoContentObserver(new Handler(Looper.getMainLooper()));
    private static VideoApplication instance;
    
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        instance = this;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        getContentResolver().registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, true, contentObserver);
        fetchContent();
    }
    
    public void fetchContent() {
        OneTimeWorkRequest fetchContentWork = new OneTimeWorkRequest.Builder(VideoFetchWorker.class).build();
        WorkManager.getInstance(this).enqueue(fetchContentWork);
    }
    
    public static VideoApplication getInstance() {
        return instance;
    }
    
    private static class VideoContentObserver extends ContentObserver {
        public VideoContentObserver(Handler handler) {
            super(handler);
        }
        
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            VideoApplication.getInstance().fetchContent();
        }
    }
    */
}