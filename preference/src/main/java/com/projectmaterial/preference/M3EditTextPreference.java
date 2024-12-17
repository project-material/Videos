package com.projectmaterial.preference;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;

public class M3EditTextPreference extends EditTextPreference {
    
    @Nullable
    private OnBindEditTextListener onBindEditTextListener;
    
    public M3EditTextPreference(@NonNull Context context) {
        super(context);
    }
    
    public M3EditTextPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    
    public M3EditTextPreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
    }
    
    public M3EditTextPreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    
    @Override
    public void setOnBindEditTextListener(@Nullable OnBindEditTextListener onBindEditTextListener) {
        this.onBindEditTextListener = onBindEditTextListener;
    }
    
    @Nullable
    OnBindEditTextListener getOnBindEditTextListener() {
        return onBindEditTextListener;
    }
}