package com.projectmaterial.videos.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.projectmaterial.videos.R;
import com.projectmaterial.videos.model.MoreInfo;
import java.util.List;

public class MoreInfoAdapter extends RecyclerView.Adapter<MoreInfoAdapter.ViewHolder> {
    private Context context;
    private final List<MoreInfo> moreInfoItems;
    
    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.moreinfo_item_title);
            description = itemView.findViewById(R.id.moreinfo_item_description);
        }
    }
    
    public MoreInfoAdapter(Context context, List<MoreInfo> moreInfoItems) {
        this.context = context;
        this.moreInfoItems = moreInfoItems;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.moreinfo_item_view, parent, false);
        return new ViewHolder(itemView);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MoreInfo moreInfo = moreInfoItems.get(position);
        holder.title.setText(moreInfo.getTitle());
        holder.description.setText(moreInfo.getDescription());
        if (moreInfo.isCopyable()) {
            holder.itemView.setOnLongClickListener(view -> {
                ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(moreInfo.getTitle(), moreInfo.getDescription());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
                return true;
            });
        }
    }
    
    @Override
    public int getItemCount() {
        return moreInfoItems.size();
    }
}