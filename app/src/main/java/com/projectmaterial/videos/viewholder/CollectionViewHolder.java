package com.projectmaterial.videos.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.projectmaterial.videos.R;

public class CollectionViewHolder extends RecyclerView.ViewHolder {
    public TextView collectionName;
    public TextView collectionCount;
    
    public CollectionViewHolder(View itemView) {
        super(itemView);
        collectionName = itemView.findViewById(R.id.name);
        collectionCount = itemView.findViewById(R.id.count);
    }
}