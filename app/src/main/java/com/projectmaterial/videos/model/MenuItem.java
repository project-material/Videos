package com.projectmaterial.videos.model;

import android.view.View;

public class MenuItem {
    private int iconResId;
    private int titleResId;
    private View.OnClickListener onClickListener;

    public MenuItem(int iconResId, int titleResId, View.OnClickListener onClickListener) {
        this.iconResId = iconResId;
        this.titleResId = titleResId;
        this.onClickListener = onClickListener;
    }

    public int getIconResId() {
        return iconResId;
    }

    public int getTitleResId() {
        return titleResId;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }
}