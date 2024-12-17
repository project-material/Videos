package com.projectmaterial.videos.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.projectmaterial.videos.R;
import com.projectmaterial.videos.activity.base.BaseActivity;
import com.projectmaterial.videos.app.VideoApplication;

public class VideoLibraryActivity extends BaseActivity {

    private static CoordinatorLayout coordinator;
    private ActivityResultLauncher<Intent> manageFilesPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCurrentActivity(this);
        setContentView(R.layout.activity_main);
        
        coordinator = findViewById(R.id.coordinator);
        coordinator.setBackgroundColor(getColor(coordinator, R.attr.colorSurface));
        
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        
        NavigationBarView navigationBarView = findViewById(R.id.navigation_bar_view);
        NavigationUI.setupWithNavController(navigationBarView, navController);
        
        setupManageFilesPermission();
        checkManageAllFilesAccessPermission();
    }
    
    @Override
    protected void restart() {
        recreate();
    }
    
    private boolean checkManageAllFilesAccessPermission() {
        if (!Environment.isExternalStorageManager()) {
            requestManageAllFilesAccessPermission();
            return false;
        }
        return true;
    }
    
    private void requestManageAllFilesAccessPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        manageFilesPermissionLauncher.launch(intent);
    }
    
    private void setupManageFilesPermission() {
        manageFilesPermissionLauncher = registerForActivityResult(new StartActivityForResult(), result -> {
            if (checkManageAllFilesAccessPermission()) {
                VideoApplication.getInstance().fetchContent();
            }
        });
    }
    
    public void showSnackbar(@NonNull CharSequence message) {
        Snackbar snackbar = Snackbar.make(coordinator, message, Snackbar.LENGTH_SHORT);
        snackbar.setAnchorView(R.id.navigation_bar_view);
        
        View snackbarView = snackbar.getView();
        
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) snackbarView.getLayoutParams();
        layoutParams.setMargins(
            layoutParams.leftMargin,
            layoutParams.topMargin,
            layoutParams.rightMargin,
            layoutParams.bottomMargin + convertDimensionToPixels(24)
        );
        
        snackbarView.setLayoutParams(layoutParams);
        snackbar.show();
    }
}