package com.projectmaterial.videos.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.PackageInfoFlags;
import android.os.Bundle;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.SwitchPreferenceCompat;

import com.google.android.material.snackbar.Snackbar;
import com.projectmaterial.preference.M3PreferenceFragment;
import com.projectmaterial.videos.R;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class SettingsFragment extends M3PreferenceFragment {
    
    private static final String PREF_KEY_THEME = "pref_key_theme";
    private static final String PREF_KEY_COLOR_SCHEME = "pref_key_color_scheme";
    private static final String PREF_KEY_CLEAR_CACHE = "pref_key_clear_cache";
    private static final String PREF_KEY_VERSION = "pref_key_version";
    
    private boolean isPreferenceChanged = false;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        
        ListPreference preferenceTheme = findPreference(PREF_KEY_THEME);
        if (preferenceTheme != null) {
            preferenceTheme.setOnPreferenceChangeListener((preference, newValue) -> {
                isPreferenceChanged = true;
                return true;
            });
        }
        
        SwitchPreferenceCompat preferenceColorScheme = findPreference(PREF_KEY_COLOR_SCHEME);
        if (preferenceColorScheme != null) {
            preferenceColorScheme.setOnPreferenceChangeListener((preference, newValue) -> {
                isPreferenceChanged = true;
                return true;
            });
        }
        
        Preference preferenceClearCache = findPreference(PREF_KEY_CLEAR_CACHE);
        if (preferenceClearCache != null) {
            preferenceClearCache.setOnPreferenceClickListener(preference -> {
                clearCache(getContext().getCacheDir());
                return true;
            });
        }
        
        Preference preferenceVersion = findPreference(PREF_KEY_VERSION);
        if (preferenceVersion != null) {
            try {
                PackageManager packageManager = getContext().getPackageManager();
                String packageName = getContext().getPackageName();
                PackageInfoFlags flags = PackageInfoFlags.of(0);
                PackageInfo packageInfo = packageManager.getPackageInfo(packageName, flags);
                preferenceVersion.setSummary(packageInfo.versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isPreferenceChanged() {
        return isPreferenceChanged;
    }
    
    private void clearCache(@NonNull File directory) {
        String error = getString(R.string.snackbar_cache_clear_error);
        String success = getString(R.string.snackbar_cache_clear_success);
        
        try {
            if (directory != null && directory.isDirectory()) {
                clearRecursively(directory);
                showSnackbar(success);
            } else {
                showSnackbar(error);
            }
        } catch (Exception e) {
            Log.e("CacheClearError", "Failed to clear cache", e);
            showSnackbar(error);
        }
    }
    
    private void clearRecursively(@NonNull File directory) throws IOException {
        if (directory.isDirectory()) {
            Files.walk(directory.toPath())
                    .map(Path::toFile)
                    .sorted(Comparator.reverseOrder())
                    .forEach(File::delete);
        }
    }
    
    private void showSnackbar(@NonNull String message) {
    	Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
    }
}