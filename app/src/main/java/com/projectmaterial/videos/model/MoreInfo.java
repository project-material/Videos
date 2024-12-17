package com.projectmaterial.videos.model;

public class MoreInfo {
    private String description;
    private String title;
    private final boolean isCopyable;
    
    public MoreInfo(String description, String title, boolean isCopyable) {
        this.description = description;
        this.title = title;
        this.isCopyable = isCopyable;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public boolean isCopyable() {
        return isCopyable;
    }
}