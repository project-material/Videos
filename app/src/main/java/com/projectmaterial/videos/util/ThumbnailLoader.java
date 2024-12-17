package com.projectmaterial.videos.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.projectmaterial.videos.R;
import com.projectmaterial.videos.database.Video;

public class ThumbnailLoader {
    
    public static void loadThumbnail(@NonNull Context context, @NonNull ImageView imageView, @NonNull Video video) {
    	String videoKey = video.getKey();
        Bitmap cachedThumbnail = BitmapCache.get(videoKey);
        Uri thumbnailUri = video.getThumbnailUri();
        
        if (cachedThumbnail != null && BitmapCache.isCached(videoKey)) {
            loadBitmapIntoView(context, cachedThumbnail, imageView, videoKey);
        } else if (thumbnailUri != null) {
            loadBitmapIntoView(context, thumbnailUri, imageView, videoKey);
        } else {
            handleNoThumbnail(imageView);
        }
    }
    
    private static void loadBitmapIntoView(@NonNull Context context, @NonNull Object resource, @NonNull ImageView imageView, @NonNull String videoKey) {
    	try {
            RequestBuilder<Bitmap> requestBuilder = Glide.with(context)
                    .asBitmap()
                    .transition(BitmapTransitionOptions.withCrossFade());
            
            if (resource instanceof Bitmap) {
                requestBuilder.load((Bitmap) resource);
            } else if (resource instanceof Uri) {
                requestBuilder.load((Uri) resource);
            }
            
            requestBuilder.into(getCustomTarget(imageView, videoKey));
        } catch (Exception e) {
            e.printStackTrace();
            handleNoThumbnail(imageView);
        }
    }
    
    private static CustomTarget<Bitmap> getCustomTarget(@NonNull ImageView imageView, @NonNull String videoKey) {
        return new CustomTarget<Bitmap>() {
            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                imageView.setBackgroundResource(R.drawable.placeholder_thumbnail);
                imageView.setImageResource(0);
            }

            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (!resource.isRecycled()) {
                    imageView.setImageBitmap(resource);
                    
                    if (!BitmapCache.isCached(videoKey)) {
                        BitmapCache.put(videoKey, resource);
                    }
                } else {
                    handleNoThumbnail(imageView);
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                imageView.setBackgroundResource(R.drawable.placeholder_thumbnail);
                imageView.setImageResource(0);
            }
        };
    }

    private static void handleNoThumbnail(@NonNull ImageView imageView) {
        imageView.setBackgroundResource(R.drawable.placeholder_thumbnail);
        imageView.setImageResource(0);
    }
}