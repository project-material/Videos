package com.projectmaterial.videos.util;

import androidx.annotation.NonNull;
import com.projectmaterial.videos.database.Video;
// import com.projectmaterial.videos.util.collection.CollectionUtils;

public class BottomSheetDialogUtils {
    
    
    
    private FavoriteStateToggleListener listener;
    
    public BottomSheetDialogUtils(FavoriteStateToggleListener listener) {
        this.listener = listener;
    }
    
    public interface FavoriteStateToggleListener {
        void toggleFavoriteState(@NonNull Video video);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*
    private static void showVideoInfoBottomSheetDialog(@NonNull Context context, @NonNull Video video) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        View contentView = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_video_info, null);
        ViewGroup itemViewContainer1 = contentView.findViewById(R.id.info_item_container_1);
        ViewGroup itemViewContainer2 = contentView.findViewById(R.id.info_item_container_2);
        View itemViewSpaceHorizontal2 = contentView.findViewById(R.id.info_space_horizontal_2);
        View itemViewSpaceHorizontal3 = contentView.findViewById(R.id.info_space_horizontal_3);
        View itemViewSpaceVertical1 = contentView.findViewById(R.id.info_space_vertical_1);
        View itemViewSpaceVertical2 = contentView.findViewById(R.id.info_space_vertical_2);
    
    
        String name = VideoUtils.getName(video);
        int nameTitle = R.string.info_file_name;
        InfoItemView itemViewFileName = contentView.findViewById(R.id.info_item_complete_name);
        InfoUtils.getInfo(itemViewFileName, name, nameTitle);
        InfoUtils.getClipboardAction(context, itemViewFileName, name, nameTitle);
    
    
        String dir = VideoUtils.getDir(video);
        int dirTitle = R.string.info_location;
        InfoItemView itemViewDir = contentView.findViewById(R.id.info_item_location);
        InfoUtils.getInfo(itemViewDir, dir, dirTitle);
        InfoUtils.getClipboardAction(context, itemViewDir, dir, dirTitle);
    
    
    
        String resolution = VideoUtils.getResolution(video);
        int resolutionTitle = R.string.info_resolution;
        InfoItemView itemViewResolution = contentView.findViewById(R.id.info_item_resolution);
        InfoUtils.getInfo(itemViewResolution, resolution, resolutionTitle);
    
    
        InfoUtils.getSpaceBetweenItemViews(itemViewSpaceVertical1, resolution);
        String bitrate = VideoUtils.getBitrate(context, video);
        int bitrateTitle = R.string.info_bitrate;
        InfoItemView itemViewBitrate = contentView.findViewById(R.id.info_item_bitrate);
        InfoUtils.getInfo(itemViewBitrate, bitrate, bitrateTitle);
        InfoUtils.getSpaceBetweenItemViews(itemViewSpaceVertical1, bitrate);
        
        String data = video.getData();
        Uri uri = Uri.parse(data);
        
        int frameRateTitle = R.string.info_frame_rate;
        InfoItemView itemViewFrameRate = contentView.findViewById(R.id.info_item_frame_rate);
        InfoUtils.getInfo(itemViewFrameRate, context.getString(R.string.info_calculating), frameRateTitle);
        MediaMetadataUtils.getFrameRate(context, uri, new MediaMetadataUtils.Callback() {
            @Override
            public void onMetadataRetrieved(String frameRate) {
                InfoUtils.getInfo(itemViewFrameRate, frameRate, frameRateTitle);
                InfoUtils.getSpaceBetweenItemViews(itemViewSpaceVertical2, frameRate);
            }
        });
        
        String codec = VideoUtils.getCodec(video);
        int codecTitle = R.string.info_codec;
        InfoItemView itemViewCodec = contentView.findViewById(R.id.info_item_codec);
        InfoUtils.getInfo(itemViewCodec, codec, codecTitle);
        InfoUtils.getSpaceBetweenItemViews(itemViewSpaceVertical2, codec);
        String duration = VideoUtils.getDuration(context, video);
        int durationTitle = R.string.info_duration;
        InfoItemView itemViewDuration = contentView.findViewById(R.id.info_item_duration);
        InfoUtils.getInfo(itemViewDuration, duration, durationTitle);
        String size = VideoUtils.getSize(context, video);
        int sizeTitle = R.string.info_size;
        InfoItemView itemViewSize = contentView.findViewById(R.id.info_item_size);
        InfoUtils.getInfo(itemViewSize, size, sizeTitle);
        InfoUtils.getInfoContainerVisibility(itemViewResolution, itemViewBitrate, itemViewContainer1, itemViewSpaceHorizontal2);
        InfoUtils.getInfoContainerVisibility(itemViewFrameRate, itemViewCodec, itemViewContainer2, itemViewSpaceHorizontal3);
       
        dialog.getWindow().setNavigationBarColor(Color.TRANSPARENT);
        dialog.setContentView(contentView);
        dialog.show();
    }
    */
}