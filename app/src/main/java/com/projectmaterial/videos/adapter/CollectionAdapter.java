package com.projectmaterial.videos.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import com.projectmaterial.videos.R;
import com.projectmaterial.videos.activity.CollectionActivity;
import com.projectmaterial.videos.bottomsheet.BottomSheetUtils;
import com.projectmaterial.videos.database.Video;
import com.projectmaterial.videos.util.CollectionUtils;
import com.projectmaterial.videos.util.LayoutType;
import com.projectmaterial.videos.util.SortingCriteria;
import com.projectmaterial.videos.util.SortingOrder;
import com.projectmaterial.videos.util.SortingUtils;
import com.projectmaterial.videos.viewholder.CollectionViewHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionViewHolder> {
    private Context context;
    private LayoutType currentLayoutType = LayoutType.LIST;
    private List<String> collections = new ArrayList<>();
    private Map<String, Integer> collectionMap = new HashMap<>();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private SortingCriteria.Collection currentSortingCriteria = SortingCriteria.Collection.COLLECTION_NAME;
    private SortingOrder currentSortingOrder = SortingOrder.ASCENDING;
    private View itemView;
    private static final String PREF_KEY_SORTING_CRITERIA = "sortingCriteria";
    private static final String PREF_KEY_SORTING_ORDER = "sortingOrder";
    private static int VIEW_TYPE_GRID = 2;
    private static int VIEW_TYPE_LIST = 1;
    
    public CollectionAdapter(Context context) {
        setHasStableIds(true);
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.sharedPreferencesEditor = sharedPreferences.edit();
        getSortingPreferences();
    }
    
    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LIST) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_collection_list, parent, false);
        } else {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_collection_grid, parent, false);
        }
        return new CollectionViewHolder(itemView);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        String collectionKey = collections.get(position);
        String collectionName = CollectionUtils.getCollectionName(context, collectionKey);
        holder.collectionName.setText(collectionName);
        String collectionCount = CollectionUtils.getCollectionCount(context, collectionKey, collectionMap);
        holder.collectionCount.setText(collectionCount);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, CollectionActivity.class);
            intent.putExtra("collection_key", collectionKey);
            context.startActivity(intent);
        });
        holder.itemView.setOnLongClickListener(view -> {
            BottomSheetUtils.showCollectionInfoDialog(context, collectionKey);
            return true;
        });
    }
    
    @Override
    public int getItemCount() {
        return collections.size();
    }
    
    @Override
    public long getItemId(int position) {
        return collections.get(position).hashCode();
    }
    
    @Override
    public int getItemViewType(int position) {
        return currentLayoutType == LayoutType.LIST ? VIEW_TYPE_LIST : VIEW_TYPE_GRID;
    }
    
    public SortingCriteria.Collection getSortingCriteria() {
        return currentSortingCriteria;
    }
    
    public SortingOrder getSortingOrder() {
        return currentSortingOrder;
    }
    
    private void getSortingPreferences() {
        String sortingCriteria = sharedPreferences.getString(PREF_KEY_SORTING_CRITERIA, null);
        if (sortingCriteria != null) currentSortingCriteria = SortingCriteria.Collection.valueOf(sortingCriteria);
        String sortingOrder = sharedPreferences.getString(PREF_KEY_SORTING_ORDER, null);
        if (sortingOrder != null) currentSortingOrder = SortingOrder.valueOf(sortingOrder);
    }
    
    private void mergeVideoCount(@NonNull List<Video> videos) {
        collectionMap.clear();
        if (videos != null) {
            for (Video video : videos) {
                String collectionPath = CollectionUtils.getCollectionPath(video);
                if (collectionPath != null) {
                    collectionMap.merge(collectionPath, 1, Integer::sum);
                }
            }
        }
    }
    
    private void saveSortingCriteria(@NonNull SortingCriteria.Collection newSortingCriteria) {
        sharedPreferencesEditor.putString(PREF_KEY_SORTING_CRITERIA, newSortingCriteria.name());
        sharedPreferencesEditor.apply();
    }
    
    private void saveSortingOrder(@NonNull SortingOrder newSortingOrder) {
        sharedPreferencesEditor.putString(PREF_KEY_SORTING_ORDER, newSortingOrder.name());
        sharedPreferencesEditor.apply();
    }
    
    public void setCollectionList(@NonNull List<String> newCollections, @NonNull List<Video> videos) {
        collections.clear();
        collections.addAll(newCollections);
        mergeVideoCount(videos);
        SortingUtils.sortCollections(context, collections, collectionMap, currentSortingCriteria, currentSortingOrder);
        notifyDataSetChanged();
    }
    
    public void setLayoutType(@NonNull LayoutType newLayoutType) {
        currentLayoutType = newLayoutType;
        notifyDataSetChanged();
    }
    
    public void updateSortingCriteria(@NonNull SortingCriteria.Collection newSortingCriteria) {
        currentSortingCriteria = newSortingCriteria;
        SortingUtils.sortCollections(context, collections, collectionMap, currentSortingCriteria, currentSortingOrder);
        saveSortingCriteria(newSortingCriteria);
        notifyDataSetChanged();
    }
    
    public void updateSortingOrder(@NonNull SortingOrder newSortingOrder) {
        currentSortingOrder = newSortingOrder;
        SortingUtils.sortCollections(context, collections, collectionMap, currentSortingCriteria, currentSortingOrder);
        saveSortingOrder(newSortingOrder);
        notifyDataSetChanged();
    }
}