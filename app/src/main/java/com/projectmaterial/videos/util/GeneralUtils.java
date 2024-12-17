package com.projectmaterial.videos.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.projectmaterial.videos.R;
import com.projectmaterial.videos.database.Video;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class GeneralUtils {

    public static void clearCabche(@NonNull Context context) {
    	String messageError = context.getString(R.string.snackbar_cache_clear_error);
        String messageSuccess = context.getString(R.string.snackbar_cache_clear_success);
        
        
        
    }
    
    
        /*
    public static void clearCache(@NonNull Context context) {
    	
        
        try {
            File cacheDir = context.getCacheDir();
            
            if (cacheDir != null && cacheDir.isDirectory()) {
                
                
                
                
                
                try {
                    Files.walk(cacheDir.toPath())
                            .map(Path::toFile)
                            .sorted(Comparator.reverseOrder())
                            .forEach(File::delete);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            
        }
    }

    private void clearCache(@NonNull Context context) {
        String messageError = getString(R.string.snackbar_cache_clear_error);
        String messageSuccess = getString(R.string.snackbar_cache_clear_success);
        
        try {
            File cacheDir = context.getCacheDir();
            if (cacheDir != null && cacheDir.isDirectory()) {
                deleteRecursive(cacheDir);
                showSnackbar(messageSuccess);
            } else {
                showSnackbar(messageError);
            }
        } catch (Exception e) {
            Log.e("CacheClearError", "Failed to clear cache", e);
            showSnackbar(messageError);
        }
    }
    
    private void showSnackbar(String message) {
    	Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show();
    }
    
    private void deleteRecursive(File directory) throws IOException {
        if (directory.isDirectory()) {
            Files.walk(directory.toPath())
                    .map(Path::toFile)
                    .sorted(Comparator.reverseOrder())
                    .forEach(File::delete);
            }
    }
    */
    
    
    
    
    
    
    public static String getVersionName(@NonNull Context context) throws NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.PackageInfoFlags.of(0));
        return packageInfo.versionName;
    }
    
    
    
    
}