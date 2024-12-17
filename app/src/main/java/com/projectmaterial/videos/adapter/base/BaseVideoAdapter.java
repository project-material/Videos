package com.projectmaterial.videos.adapter.base;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.projectmaterial.videos.util.SortingCriteria;
import com.projectmaterial.videos.util.SortingOrder;

public abstract class BaseVideoAdapter<T extends ViewHolder> extends Adapter<T> {
    
    @Override
    public abstract void onBindViewHolder(@NonNull T holder, int position);
    
    public abstract SortingCriteria.Video getSortingCriteria();
    
    public abstract SortingOrder getSortingOrder();
    
    public abstract void updateSortingCriteria(@NonNull SortingCriteria.Video newSortingCriteria);
    
    public abstract void updateSortingOrder(@NonNull SortingOrder newSortingOrder);
}