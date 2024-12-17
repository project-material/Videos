package com.projectmaterial.videos.bottomsheet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.projectmaterial.videos.R;
import com.projectmaterial.videos.activity.CollectionActivity;
import com.projectmaterial.videos.activity.VideoLibraryActivity;
import com.projectmaterial.videos.adapter.MenuItemAdapter;
import com.projectmaterial.videos.adapter.MoreInfoAdapter;
import com.projectmaterial.videos.app.VideoApplication;
import com.projectmaterial.videos.database.Video;
import com.projectmaterial.videos.model.MenuItem;
import com.projectmaterial.videos.model.MoreInfo;
import com.projectmaterial.videos.recyclerview.GridSpacingItemDecoration;
import com.projectmaterial.videos.recyclerview.NonScrollableGridLayoutManager;
import com.projectmaterial.videos.recyclerview.NonScrollableLinearLayoutManager;
import com.projectmaterial.videos.util.CollectionUtils;
import com.projectmaterial.videos.util.DialogUtils;
import com.projectmaterial.videos.util.MediaMetadataHelper;
import com.projectmaterial.videos.util.ThumbnailLoader;
import com.projectmaterial.videos.util.VideoOptionUtils;
import com.projectmaterial.videos.util.VideoUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BottomSheetUtils {
    
    public static void showCollectionInfoDialog(Context context, String collection) {
    	BottomSheetDialog dialog = new BottomSheetDialog(context);
        View contentView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_moreinfo_collection, null);
        
        TextView collectionName = contentView.findViewById(R.id.collection_name);
        collectionName.setText(CollectionUtils.getCollectionName(context, collection));
        
        List<MoreInfo> moreInfoItems = new ArrayList<>();
        
        String locationDescription = CollectionUtils.getCollectionPath(collection);
        String locationTitle = context.getString(R.string.info_location);
        MoreInfo locationInfo = new MoreInfo(locationDescription, locationTitle, true);
        moreInfoItems.add(locationInfo);
        
        GridLayoutManager gridLayoutManager = new NonScrollableGridLayoutManager(context, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int size = moreInfoItems.size();
                
                if (position < 0 || position >= size) {
                    return 1;
                }
                
                MoreInfo moreInfo = moreInfoItems.get(position);
                
                if (moreInfo.equals(locationInfo)) {
                    return 2;
                }
                
                return 1;
            }
        });
        
        MoreInfoAdapter adapter = new MoreInfoAdapter(context, moreInfoItems);
        RecyclerView recyclerView = contentView.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(context, 2, gridLayoutManager));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        
        dialog.getWindow().setNavigationBarColor(Color.TRANSPARENT);
        dialog.setContentView(contentView);
        dialog.show();
    }
    
    public static void showVideoInfoDialog(@NonNull Context context, @NonNull Video video) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View contentView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_moreinfo_video, null);
        
        List<MoreInfo> moreInfoItems = new ArrayList<>();
        
        String nameDescription = VideoUtils.getName(video);
        String nameTitle = context.getString(R.string.info_file_name);
        MoreInfo nameInfo = new MoreInfo(nameDescription, nameTitle, true);
        moreInfoItems.add(nameInfo);
        
        String locationDescription = VideoUtils.getLocation(video);
        String locationTitle = context.getString(R.string.info_location);
        MoreInfo locationInfo = new MoreInfo(locationDescription, locationTitle, true);
        moreInfoItems.add(locationInfo);
        
        String resolutionDescription = VideoUtils.getResolution(video);
        String resolutionTitle = context.getString(R.string.info_resolution);
        MoreInfo resolutionInfo = new MoreInfo(resolutionDescription, resolutionTitle, false);
        moreInfoItems.add(resolutionInfo);
        
        String bitrateDescription = context.getString(R.string.info_calculating);
        String bitrateTitle = context.getString(R.string.info_bitrate);
        MoreInfo bitrateInfo = new MoreInfo(bitrateDescription, bitrateTitle, false);
        moreInfoItems.add(bitrateInfo);
        
        String frameRateDescription = context.getString(R.string.info_calculating);
        String frameRateTitle = context.getString(R.string.info_frame_rate);
        MoreInfo frameRateInfo = new MoreInfo(frameRateDescription, frameRateTitle, false);
        moreInfoItems.add(frameRateInfo);
        
        String codecDescription = VideoUtils.getCodec(video);
        String codecTitle = context.getString(R.string.info_codec);
        MoreInfo codecInfo = new MoreInfo(codecDescription, codecTitle, false);
        moreInfoItems.add(codecInfo);
        
        String durationDescription = VideoUtils.getDuration(context, video);
        String durationTitle = context.getString(R.string.info_duration);
        MoreInfo durationInfo = new MoreInfo(durationDescription, durationTitle, false);
        moreInfoItems.add(durationInfo);
        
        String sizeDescription = VideoUtils.getSize(context, video);
        String sizeTitle = context.getString(R.string.info_size);
        MoreInfo sizeInfo = new MoreInfo(sizeDescription, sizeTitle, false);
        moreInfoItems.add(sizeInfo);
        
        GridLayoutManager gridLayoutManager = new NonScrollableGridLayoutManager(context, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int size = moreInfoItems.size();
                
                if (position < 0 || position >= size) {
                    return 1;
                }
                
                MoreInfo moreInfo = moreInfoItems.get(position);
                
                if (moreInfo.equals(nameInfo) || moreInfo.equals(locationInfo)) {
                    return 2;
                }
                
                if (moreInfo.equals(codecInfo) && !moreInfoItems.contains(frameRateInfo)) {
                    return 2;
                }
                
                return 1;
            }
        });
        
        MoreInfoAdapter adapter = new MoreInfoAdapter(context, moreInfoItems);
        RecyclerView recyclerView = contentView.findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(context, 2, gridLayoutManager));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        
        MediaMetadataHelper.fetchBitrate(context, video, bitrate -> {
            int bitrateIndex = moreInfoItems.indexOf(bitrateInfo);
            
            if (bitrate == null || bitrate.isEmpty()) {
                if (bitrateIndex != -1) {
                    recyclerView.setItemAnimator(null);
                    moreInfoItems.remove(bitrateInfo);
                    adapter.notifyItemRemoved(bitrateIndex);
                }
            } else {
                bitrateInfo.setDescription(bitrate);
                if (bitrateIndex != -1) {
                    adapter.notifyItemChanged(bitrateIndex);
                }
            }
        });
        
        MediaMetadataHelper.fetchFrameRate(context, video, frameRate -> {
            int frameRateIndex = moreInfoItems.indexOf(frameRateInfo);
            
            if (frameRate == null || frameRate.isEmpty()) {
                if (frameRateIndex != -1) {
                    recyclerView.setItemAnimator(null);
                    moreInfoItems.remove(frameRateInfo);
                    adapter.notifyItemRemoved(frameRateIndex);
                }
            } else {
                frameRateInfo.setDescription(frameRate);
                if (frameRateIndex != -1) {
                    adapter.notifyItemChanged(frameRateIndex);
                }
            }
        });
        
        dialog.getWindow().setNavigationBarColor(Color.TRANSPARENT);
        dialog.setContentView(contentView);
        dialog.show();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    private static void setupDuration(@NonNull Context context, @NonNull Video video, @NonNull View contentView) {
        TextView durationTextView = contentView.findViewById(R.id.duration);
        durationTextView.setText(VideoUtils.getDuration(context, video));
    }
    
    private static void setupSubtitle(@NonNull Video video, @NonNull View contentView) {
        TextView subtitleTextView = contentView.findViewById(R.id.subtitle);
        List<String> subtitles = VideoUtils.extractSubtitle(video.getDisplayName());
        
        if (!subtitles.isEmpty()) {
            subtitleTextView.setText(subtitles.stream().collect(Collectors.joining(" ")));
            subtitleTextView.setVisibility(View.VISIBLE);
        } else {
            subtitleTextView.setVisibility(View.GONE);
        }
    }
    
    private static void setupThumbnail(@NonNull Context context, @NonNull Video video, @NonNull View contentView) {
        ImageView thumbnailImageView = contentView.findViewById(R.id.thumbnail_image);
        String data = video.getData();
        
        if (data != null && !data.isEmpty()) {
            ThumbnailLoader.loadThumbnail(context, thumbnailImageView, video);
            thumbnailImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            thumbnailImageView.setImageDrawable(null);
        }
    }
    
    private static void setupTitle(@NonNull Video video, @NonNull View contentView) {
        TextView titleTextView = contentView.findViewById(R.id.title);
        titleTextView.setText(VideoUtils.extractTitle(video.getDisplayName()));
    }
    
    public static void showVideoOptionsDialog(@NonNull Context context, @NonNull Video video, @NonNull OnFavoriteStateChangeListener listener) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View contentView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_options_menu, null);
        Activity currentActivity = getCurrentActivity();
        
        // Bind video metadata
        setupDuration(context, video, contentView);
        setupSubtitle(video, contentView);
        setupThumbnail(context, video, contentView);
        setupTitle(video, contentView);
        
        // Build menu items and attach to RecyclerView
        List<MenuItem> menuItems = new ArrayList<>();
        boolean isFavorite = video.isFavorite();
        
        // Add favorite/unfavorite action
        menuItems.add(new MenuItem(
                isFavorite ? R.drawable.quantum_ic_star_filled_vd_theme_24 : R.drawable.quantum_ic_star_vd_theme_24,
                isFavorite ? R.string.option_favorite_remove : R.string.option_favorite_add,
                view -> {
                    listener.onFavoriteStateChanged(video);
                    showFavoriteSnackbar(context, video, currentActivity);
                    dialog.dismiss();
                }
        ));
        
        // Add rename action
        menuItems.add(new MenuItem(
                R.drawable.quantum_ic_edit_vd_theme_24,
                R.string.option_rename,
                view -> {
                    DialogUtils.showRenameVideoDialog(context, video);
                    dialog.dismiss();
                }
        ));
        
        // Add share action
        menuItems.add(new MenuItem(
                R.drawable.quantum_ic_share_vd_theme_24,
                R.string.option_share,
                view -> {
                    VideoOptionUtils.shareVideo(context, video);
                    dialog.dismiss();
                }
        ));
        
        // Add info action
        menuItems.add(new MenuItem(
                R.drawable.quantum_ic_info_vd_theme_24,
                R.string.option_info,
                view -> {
                    showVideoInfoDialog(context, video);
                    dialog.dismiss();
                }
        ));
        
        // Add delete action
        menuItems.add(new MenuItem(
                R.drawable.quantum_ic_delete_vd_theme_24,
                R.string.option_delete,
                view -> {
                    DialogUtils.showDeleteConfirmationDialog(context, video);
                    dialog.dismiss();
                }
        ));
        
        RecyclerView recyclerView = contentView.findViewById(R.id.recycler_view);
        MenuItemAdapter adapter = new MenuItemAdapter(menuItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new NonScrollableLinearLayoutManager(context));
        recyclerView.setNestedScrollingEnabled(false);
        
        // Customize dialog appearance and show
        dialog.getWindow().setNavigationBarColor(Color.TRANSPARENT);
        dialog.setContentView(contentView);
        dialog.show();
    }
    
    private static void showFavoriteSnackbar(@NonNull Context context, @NonNull Video video, @NonNull Activity currentActivity) {
        String message = video.isFavorite() 
            ? context.getString(R.string.snackbar_favorite_added, video.getDisplayName()) 
            : context.getString(R.string.snackbar_favorite_removed, video.getDisplayName());
        
        if (currentActivity instanceof VideoLibraryActivity) {
            ((VideoLibraryActivity) currentActivity).showSnackbar(message);
        } else if (currentActivity instanceof CollectionActivity) {
            ((CollectionActivity) currentActivity).showSnackbar(message);
        }
    }
    
    public interface OnFavoriteStateChangeListener {
        void onFavoriteStateChanged(@NonNull Video video);
    }
    
    private static Activity getCurrentActivity() {
    	return VideoApplication.getInstance().getCurrentActivity();
    }
}