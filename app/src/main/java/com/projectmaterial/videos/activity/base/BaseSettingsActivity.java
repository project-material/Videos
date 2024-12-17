package com.projectmaterial.videos.activity.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.text.LineBreakConfig;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.projectmaterial.videos.R;

public class BaseSettingsActivity extends BaseActivity {
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        AppBarLayout appBar = findViewById(R.id.app_bar);
        AppBarLayout.Behavior appBarBehavior = new AppBarLayout.Behavior();
        appBarBehavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                Resources resources = appBarLayout.getResources();
                Configuration configuration = resources.getConfiguration();
                return configuration.orientation == Configuration.ORIENTATION_LANDSCAPE;
            }
        });
        
        CoordinatorLayout coordinator = findViewById(R.id.coordinator);
        coordinator.setBackgroundColor(getColor(coordinator, R.attr.colorSurface));
        CoordinatorLayout.LayoutParams coordinatorParams = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
        coordinatorParams.setBehavior(appBarBehavior);
        
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setLineSpacingMultiplier(1.1f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            collapsingToolbar.setHyphenationFrequency(Layout.HYPHENATION_FREQUENCY_NORMAL_FAST);
            collapsingToolbar.setStaticLayoutBuilderConfigurer(staticLayoutBuilderConfigurer -> {
                staticLayoutBuilderConfigurer.setLineBreakConfig(new LineBreakConfig.Builder()
                        .setLineBreakWordStyle(LineBreakConfig.LINE_BREAK_WORD_STYLE_PHRASE)
                        .build());
            });
        }
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.quantum_ic_arrow_back_vd_theme_24);
        setSupportActionBar(toolbar);
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
}