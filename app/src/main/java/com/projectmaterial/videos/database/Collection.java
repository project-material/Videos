package com.projectmaterial.videos.database;

import android.os.Environment;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Collection {
    private String path;
    private List<Video> videos;
    private int videoCount;

    public Collection(String path) {
        this.path = path;
        this.videos = new ArrayList<>();
        this.videoCount = 0;
    }
    
    public String getName() {
        int lastIndex = path.lastIndexOf(File.separator);
        return path.substring(lastIndex + 1);
    }

    public String getPath() {
        return path;
    }
    
    public List<Video> getVideos() {
        return videos;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public void addVideo(Video video) {
        videos.add(video);
        videoCount++;
    }
}