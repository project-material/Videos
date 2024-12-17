package com.projectmaterial.preference;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

public class M3TopIntroPreference extends Preference {
    
    public M3TopIntroPreference(@NonNull Context context) {
        this(context, null);
    }
    
    public M3TopIntroPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.m3_preference_top_intro);
        setSelectable(false);
    }
    
    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        holder.setDividerAllowedAbove(false);
        holder.setDividerAllowedBelow(false);
    }
}