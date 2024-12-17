package com.projectmaterial.videos.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.projectmaterial.videos.database.Video;
import com.projectmaterial.videos.bottomsheet.BottomSheetUtils;
import com.projectmaterial.videos.viewholder.VideoViewHolder;
import java.util.List;

public class AdapterUtils {
    
    
    
    public static void setItemDuration(@NonNull Context context, @NonNull VideoViewHolder holder, @NonNull Video video) {
        String duration = VideoUtils.getDuration(context, video);
        TextView durationTextView = holder.durationTextView;
        durationTextView.setText(duration);
    }
    
    public static void setItemSubtitle(@NonNull VideoViewHolder holder, @NonNull Video video) {
        String displayName = video.getDisplayName();
        TextView subtitleTextView = holder.subtitleTextView;
        List<String> extractedCharacters = VideoUtils.extractSubtitle(displayName);
        if (!extractedCharacters.isEmpty()) {
            String subtitle = TextUtils.join(" ", extractedCharacters);
            subtitleTextView.setText(subtitle);
            subtitleTextView.setVisibility(View.VISIBLE);
        } else {
            subtitleTextView.setVisibility(View.GONE);
        }
    }
    
    public static void setItemThumbnail(@NonNull Context context, @NonNull VideoViewHolder holder, @NonNull Video video) {
        ImageView thumbnailImageView = holder.thumbnailImageView;
        ThumbnailLoader.loadThumbnail(context, thumbnailImageView, video);
        thumbnailImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }
    
    /*
    public static void setItemThumbnail(@NonNull Context context, @NonNull VideoViewHolder holder, @NonNull Video video) {
        String data = video.getData();
        ImageView thumbnailImageView = holder.thumbnailImageView;
        if (data != null && !data.isEmpty()) {
            ThumbnailLoader.getThumbnailBitmap(context, thumbnailImageView, video);
            thumbnailImageView.setScaleType(ScaleType.CENTER_CROP);
        } else {
            thumbnailImageView.setImageDrawable(null);
        }
    }
    */
    
    public static void setItemTitle(@NonNull VideoViewHolder holder, @NonNull Video video) {
        String displayName = video.getDisplayName();
        TextView titleTextView = holder.titleTextView;
        titleTextView.setText(VideoUtils.extractTitle(displayName));
    }
    
    public static void setOnItemClickListener(@NonNull Context context, @NonNull VideoViewHolder holder, @NonNull Video video) {
        View itemView = holder.itemView;
        itemView.setOnClickListener(view -> {
                VideoOptionUtils.openVideoWith(context, video);
           // VideoOptionUtils.openVideo(context, video);
        });
    }
    
    public static void setOnItemLongClickListener(@NonNull Context context, @NonNull VideoViewHolder holder, @NonNull Video video, BottomSheetUtils.OnFavoriteStateChangeListener listener) {
        View itemView = holder.itemView;
        itemView.setOnLongClickListener(view -> {
            BottomSheetUtils.showVideoOptionsDialog(context, video, listener);
            return true;
        });{}
    }
}