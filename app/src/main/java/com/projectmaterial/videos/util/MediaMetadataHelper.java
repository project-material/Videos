package com.projectmaterial.videos.util;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.annotation.WorkerThread;

import com.projectmaterial.videos.R;
import com.projectmaterial.videos.database.Video;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class MediaMetadataHelper {
    
    public interface MetadataCallback {
        @UiThread
        void onMetadataReady(String metadata);
    }

    /**
     * Retrieves the bitrate of a video asynchronously.
     *
     * @param context  the context for resource access.
     * @param video    the video object containing the data source.
     * @param callback the callback to return the result.
     */
    public static void fetchBitrate(@NonNull Context context, @NonNull Video video, MetadataCallback callback) {
        CompletableFuture.supplyAsync(() -> retrieveBitrate(context, video))
                .thenAccept(result -> postResultToMainThread(callback, result));
    }

    /**
     * Retrieves the frame rate of a video asynchronously.
     *
     * @param context  the context for resource access.
     * @param video    the video object containing the data source.
     * @param callback the callback to return the result.
     */
    public static void fetchFrameRate(@NonNull Context context, @NonNull Video video, MetadataCallback callback) {
        CompletableFuture.supplyAsync(() -> retrieveFrameRate(context, video))
                .thenAccept(result -> postResultToMainThread(callback, result));
    }

    @WorkerThread
    private static String retrieveBitrate(Context context, Video video) {
        String videoPath = video.getData();
        String bitrateStr = null;

        try (MediaMetadataRetriever retriever = new MediaMetadataRetriever()) {
            retriever.setDataSource(videoPath);
            bitrateStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bitrateStr == null || bitrateStr.isEmpty()) {
            return " ";
        }

        try {
            int bitrate = Integer.parseInt(bitrateStr);
            return formatBitrate(bitrate, context);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return " ";
        }
    }

    @WorkerThread
    private static String retrieveFrameRate(Context context, Video video) {
        String videoPath = video.getData();
        String frameRateStr = null;
        FFmpegMediaMetadataRetriever retriever = null;
        
        try {
            retriever = new FFmpegMediaMetadataRetriever();
            retriever.setDataSource(videoPath);
            frameRateStr = retriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_FRAMERATE);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            if (retriever != null) {
                retriever.release();
            }
        }
        
        if (frameRateStr == null || frameRateStr.isEmpty()) {
            return "";
        } else {
            return frameRateStr + " " + context.getString(R.string.info_frame_rate_fps);
        }
    }

    @UiThread
    private static void postResultToMainThread(MetadataCallback callback, String result) {
        new Handler(Looper.getMainLooper()).post(() -> callback.onMetadataReady(result));
    }

    private static String formatBitrate(long bitrate, Context context) {
        String[] units = context.getResources().getStringArray(R.array.bitrate_units);

        if (bitrate < 1000) {
            return bitrate + " " + units[0]; // bits per second
        } else if (bitrate < 1000000) {
            double kbps = bitrate / 1000.0;
            return String.format(Locale.getDefault(), "%.2f %s", kbps, units[1]); // kilobits per second
        } else if (bitrate < 1000000000) {
            double mbps = bitrate / 1000000.0;
            return String.format(Locale.getDefault(), "%.2f %s", mbps, units[2]); // megabits per second
        } else {
            double gbps = bitrate / 1000000000.0;
            return String.format(Locale.getDefault(), "%.2f %s", gbps, units[3]); // gigabits per second
        }
    }
}