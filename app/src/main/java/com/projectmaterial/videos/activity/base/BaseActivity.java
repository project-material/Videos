package com.projectmaterial.videos.activity.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.google.android.material.color.DynamicColors;
import com.google.android.material.color.MaterialColors;
import com.projectmaterial.videos.app.VideoApplication;

public class BaseActivity extends AppCompatActivity 
    implements SharedPreferences.OnSharedPreferenceChangeListener {
    
    private static final String PREF_KEY_COLOR_SCHEME = "pref_key_color_scheme";
    public static final String PREF_KEY_NIGHT_MODE = "pref_key_theme";
    
    public SharedPreferences sharedPreferences;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        enableEdgeToEdgeNoContrast();
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        applyDynamicColors();
        applyNightMode();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
    
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
        if (PREF_KEY_NIGHT_MODE.equals(key)) {
            restart();
        } else if (PREF_KEY_COLOR_SCHEME.equals(key)) {
            restartWithDelay();
        }
    }
    
    private void applyDynamicColors() {
    	if (sharedPreferences.getBoolean(PREF_KEY_COLOR_SCHEME, true)) {
            DynamicColors.applyToActivityIfAvailable(this);
        }
    }
    
    public void applyNightMode() {
    	String nightMode = sharedPreferences.getString(PREF_KEY_NIGHT_MODE, "0");
        
        switch (nightMode) {
            case "0":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case "1":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "2":
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
    
    @SuppressWarnings("deprecation")
    public void overrideTransition() {
    	int enterAnim = android.R.anim.fade_in;
        int exitAnim = android.R.anim.fade_out;
        overridePendingTransition(enterAnim, exitAnim);
    }
    
    protected void restart() {
        // TODO: Nothing
    }
    
    protected void restartWithDelay() {
        new Handler(Looper.getMainLooper()).postDelayed(this::restart, 250);
    }
    
    public void setCurrentActivity(@NonNull Activity activity) {
        VideoApplication.getInstance().setCurrentActivity(activity);
    }

    @ColorInt
    public static int getColor(@NonNull View view, @AttrRes int colorAttributeResId) {
    	return MaterialColors.getColor(view, colorAttributeResId);
    }
    
    @DimenRes
    public static int convertDimensionToPixels(int dpValue) {
        return (int) (dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    private void enableEdgeToEdgeNoContrast() {
        SystemBarStyle systemBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT);
        EdgeToEdge.enable(this, systemBarStyle);
        getWindow().setNavigationBarContrastEnforced(false);
    }
}