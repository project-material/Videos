package com.projectmaterial.videos.worker;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.projectmaterial.videos.database.Video;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VideoFetchWorker extends Worker {
    private static final String TAG = VideoFetchWorker.class.getSimpleName();
    private static final MutableLiveData<List<Video>> videosLiveData = new MutableLiveData<>();
    
    private static final String[] PROJECTION = {
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.DURATION
    };
    
    public VideoFetchWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    
    @Override
    public @NonNull Result doWork() {
        List<Video> videos = fetchVideos();
        if (videos != null) {
            videosLiveData.postValue(videos);
            return Result.success();
        } else {
            Log.e(TAG, "Failed to fetch videos from the media store.");
            return Result.failure();
        }
    }

    private List<Video> fetchVideos() {
        try {
            return queryMediaStoreVideos(getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG, "Error fetching videos from MediaStore", e);
            return null;
        }
    }
    
    private List<Video> queryMediaStoreVideos(@NonNull Context context) {
        List<Video> videos = new ArrayList<>();
        Set<Long> videoIds = new HashSet<>();

        try (Cursor cursor = context.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, 
                PROJECTION, 
                null, 
                null, 
                null)) {

            if (cursor == null) {
                Log.w(TAG, "Cursor is null. MediaStore query failed.");
                return videos;
            }

            int idIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            int displayNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idIndex);
                
                if (!videoIds.add(id)) {
                    continue; // Skip if video already processed
                }
                
                String data = cursor.getString(dataIndex);
                String displayName = cursor.getString(displayNameIndex);
                long duration = cursor.getLong(durationIndex) / 1000; // Convert milliseconds to seconds

                if (isValidVideo(data, duration)) {
                    videos.add(new Video(id, data, displayName, duration));
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Error reading from MediaStore", e);
        }

        return videos;
    }

    private boolean isValidVideo(String data, long duration) {
        return data != null && !data.isEmpty() && duration > 0;
    }

    public static LiveData<List<Video>> getVideosLiveData() {
        return videosLiveData;
    }
}