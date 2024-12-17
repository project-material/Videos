package com.projectmaterial.videos.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import com.projectmaterial.videos.R;
import com.projectmaterial.videos.activity.CollectionActivity;
import com.projectmaterial.videos.activity.VideoLibraryActivity;
import com.projectmaterial.videos.app.VideoApplication;
import com.projectmaterial.videos.database.Video;
import java.io.File;

public class VideoOptionUtils {
    
    /*
    public static void openVideo(@NonNull Context context, @NonNull Video video) {
        String videoData = video.getData();
        if (videoData != null && !videoData.isEmpty()) {
            File videoFile = new File(videoData);
            if (videoFile.exists()) {
                Uri videoUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", videoFile);
                Intent openIntent = new Intent(context, PlayerActivity.class);
                openIntent.setData(videoUri);
                openIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(openIntent);
            }
        }
    }
    */
    
    public static void openVideoWith(Context context, Video video) {
        String data = video.getData();
        if (data != null && !data.isEmpty()) {
            File file = new File(data);
            if (file.exists()) {
                Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndTypeAndNormalize(uri, "video/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            }
        }
    }
    
    public static boolean renameVideo(String path, String newPath) {
        File videoFile = new File(path);
        File renamedVideoFile = new File(newPath);
        if (videoFile.exists()) {
            return videoFile.renameTo(renamedVideoFile);
        } else {
            return false;
        }
    }
    
    public static void shareVideo(@NonNull Context context, @NonNull Video video) {
        String videoData = video.getData();
        if (videoData != null && !videoData.isEmpty()) {
            File videoFile = new File(videoData);
            if (videoFile.exists()) {
                Uri videoUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".fileprovider", videoFile);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("video/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, videoUri);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(Intent.createChooser(shareIntent, null));
            }
        }
    }
    
    public static void deleteVideo(@NonNull Context context, @NonNull Video video) {
        String data = video.getData();
        String displayName = video.getDisplayName();
        File videoFile = new File(data);
        if (videoFile.exists()) {
            boolean isDeleted = videoFile.delete();
            if (isDeleted) {
                BitmapCache.removeFromCache(data);
                showSnackbar(context.getString(R.string.snackbar_delete_success, displayName));
            } else {
                showSnackbar(context.getString(R.string.snackbar_delete_error));
            }
        } else {
            showSnackbar(context.getString(R.string.snackbar_delete_error_not_found));
        }
    }
    
    private static void showSnackbar(@NonNull String message) {
        Activity currentActivity = VideoApplication.getInstance().getCurrentActivity();
        if (currentActivity instanceof VideoLibraryActivity) {
            ((VideoLibraryActivity) currentActivity).showSnackbar(message);
        } else if (currentActivity instanceof CollectionActivity) {
            ((CollectionActivity) currentActivity).showSnackbar(message);
        }
    }
}