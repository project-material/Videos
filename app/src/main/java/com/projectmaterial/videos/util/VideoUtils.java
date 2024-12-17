package com.projectmaterial.videos.util;

import android.content.Context;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import androidx.annotation.NonNull;
import com.projectmaterial.videos.R;
import com.projectmaterial.videos.database.Video;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoUtils {
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private static String formatBitrate(long bitrate, Context context) {
        String[] units = context.getResources().getStringArray(R.array.bitrate_units);

        if (bitrate < 1000) {
            return bitrate + " " + units[0];
        } else if (bitrate < 1000000) {
            double kbps = bitrate / 1000.0;
            return String.format(Locale.getDefault(), "%.2f %s", kbps, units[1]);
        } else if (bitrate < 1000000000) {
            double mbps = bitrate / 1000000.0;
            return String.format(Locale.getDefault(), "%.2f %s", mbps, units[2]);
        } else {
            double gbps = bitrate / 1000000000.0;
            return String.format(Locale.getDefault(), "%.2f %s", gbps, units[3]);
        }
    }
    
    public static List<String> extractSubtitle(@NonNull String name) {
        List<String> subtitles = new ArrayList<>();
        Pattern pattern = Pattern.compile("^\\s*\\[([^\\[\\]]+)\\]");
        Matcher matcher = pattern.matcher(name);
        while (matcher.find()) {
            subtitles.add(matcher.group(1).trim());
        }
        return subtitles;
    }
    
    public static String extractTitle(@NonNull String name) {
        String extractedTitle = name.replaceAll("^\\[([^\\[\\]]+)\\]\\s*", "");
        return extractedTitle.trim();
    }
    
    public static String getBitrate(@NonNull Context context, @NonNull Video video) {
        String videoData = video.getData();
        String videoBitrate = "";
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(videoData);
            videoBitrate = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        if (videoBitrate.isEmpty()) {
            return "";
        }
        
        int bitrate = Integer.parseInt(videoBitrate);
        
        return formatBitrate(bitrate, context);
    }
    
    
    
    public static String getCodec(@NonNull Video video) {
        String data = video.getData();
        String codec = "";
        MediaExtractor extractor = new MediaExtractor();
        try {
            extractor.setDataSource(data);
            for (int i = 0; i < extractor.getTrackCount(); i++) {
                MediaFormat format = extractor.getTrackFormat(i);
                String mimeType = format.getString(MediaFormat.KEY_MIME);
                if (mimeType.startsWith("video/")) {
                    codec = format.containsKey(MediaFormat.KEY_MIME) ? format.getString(MediaFormat.KEY_MIME).substring(6) : "";
                    switch (codec) {
                        case "av01":
                            return "AV1";
                        case "x-vnd.on2.vp8":
                            return "VP8";
                        case "x-vnd.on2.vp9":
                            return "VP9";
                        case "avc":
                            return "AVC/H.264";
                        case "hevc":
                            return "HEVC/H.265";
                        default:
                            return codec;
                    }
                }
            }
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            extractor.release();
        }
    }
    
    public static long getDate(@NonNull Video video) {
        File file = getVideo(video);
        long lastModified = file.lastModified();
        return lastModified;
    }
    
    public static String getLocation(@NonNull Video video) {
        File file = getVideo(video);
        File dir = file.getParentFile();
        return dir.getAbsolutePath() + "/";
    }
    
    public static String getDuration(@NonNull Context context, @NonNull Video video) {
        long duration = video.getDuration();
        
        long hours = duration / 3600;
        long minutes = (duration % 3600) / 60;
        long seconds = duration % 60;
        
        String[] formattedDuration = context.getResources().getStringArray(R.array.formatted_duration);
        
        if (hours > 0) {
            return String.format(formattedDuration[0], hours, minutes, seconds);
        } else if (minutes >= 10) {
            return String.format(formattedDuration[1], minutes, seconds);
        } else {
            return String.format(formattedDuration[2], minutes, seconds);
        }
    }
    
    public static String getDisplayNameWithoutSquareBrackets(@NonNull Video video) {
        String displayName = video.getDisplayName();
        List<String> extractedCharacters = extractSubtitle(displayName);
        String subtitle = TextUtils.join(" ", extractedCharacters);
        String title = extractTitle(displayName);
        return !subtitle.isEmpty() ? subtitle + " " + title : title;
    }
    
    
    
    public static String getName(@NonNull Video video) {
        File file = getVideo(video);
        String name = file.getName();
        return name;
    }
    
    
    public static String getResolution(@NonNull Video video) {
        String videoData = video.getData();
        String videoWidth = "";
        String videoHeight = "";
        MediaExtractor extractor = new MediaExtractor();
        try {
            extractor.setDataSource(videoData);
            for (int i = 0; i < extractor.getTrackCount(); i++) {
                MediaFormat format = extractor.getTrackFormat(i);
                String mime = format.getString(MediaFormat.KEY_MIME);
                if (mime.startsWith("video/")) {
                    videoWidth = format.containsKey(MediaFormat.KEY_WIDTH) ? String.valueOf(format.getInteger(MediaFormat.KEY_WIDTH)) : "";
                    videoHeight = format.containsKey(MediaFormat.KEY_HEIGHT) ? String.valueOf(format.getInteger(MediaFormat.KEY_HEIGHT)) : "";
                    break;
                }
            }
            return (!videoWidth.isEmpty() && !videoHeight.isEmpty()) ? (videoWidth + "x" + videoHeight) : "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            extractor.release();
        }
    }
    
    
    
    
    public static String getSize(@NonNull Context context, @NonNull Video video) {
        File file = getVideo(video);
        String[] units = context.getResources().getStringArray(R.array.size_units);
        int i = 0;
        float size = file.length();
        while (size >= 1024 && i < units.length - 1) {
            size /= 1024;
            i++;
        }
        return String.format(Locale.getDefault(), "%.2f %s", size, units[i]);
    }
    
    private static File getVideo(@NonNull Video video) {
        String data = video.getData();
        return new File(data);
    }
    
    
    
    
}