package com.projectmaterial.videos.activity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import com.projectmaterial.videos.R;
import com.projectmaterial.videos.activity.base.BaseActivity;
import com.projectmaterial.videos.fragment.CollectionFragment;
import com.projectmaterial.videos.util.CollectionUtils;

public class CollectionActivity extends BaseActivity {
    
    private CoordinatorLayout coordinator;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentActivity(this);
        setContentView(R.layout.activity_collection);
        
        String collectionKey = getIntent().getStringExtra("collection_key");
        
        coordinator = findViewById(R.id.coordinator);
        coordinator.setBackgroundColor(getColor(coordinator, R.attr.colorSurface));
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.quantum_ic_arrow_back_vd_theme_24);
        toolbar.setTitle(CollectionUtils.getCollectionName(this, collectionKey));
        setSupportActionBar(toolbar);
        
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, CollectionFragment.newInstance(collectionKey))
                .commitNow();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    public void showSnackbar(@NonNull CharSequence message) {
        Snackbar.make(coordinator, message, Snackbar.LENGTH_SHORT).show();
    }
}