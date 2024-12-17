package com.projectmaterial.videos.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.projectmaterial.videos.R;

public class VideoViewHolder extends RecyclerView.ViewHolder {
    public ImageView thumbnailImageView;
    public TextView durationTextView;
    public TextView subtitleTextView;
    public TextView titleTextView;
    
    public VideoViewHolder(View itemView) {
        super(itemView);
        thumbnailImageView = (ImageView) itemView.findViewById(R.id.thumbnail_image);
        durationTextView = (TextView) itemView.findViewById(R.id.duration);
        subtitleTextView = (TextView) itemView.findViewById(R.id.subtitle);
        titleTextView = (TextView) itemView.findViewById(R.id.title);
    }
}