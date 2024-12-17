package com.projectmaterial.preference;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.MultiSelectListPreference;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class M3MultiSelectListPreferenceDialogFragment extends M3PreferenceDialogFragment {
    
    private static final String SAVE_STATE_VALUES = "M3MultiSelectListPreferenceDialogFragment.values";
    private static final String SAVE_STATE_CHANGED = "M3MultiSelectListPreferenceDialogFragment.changed";
    private static final String SAVE_STATE_ENTRIES = "M3MultiSelectListPreferenceDialogFragment.entries";
    private static final String SAVE_STATE_ENTRY_VALUES = "M3MultiSelectListPreferenceDialogFragment.entryValues";
    private CharSequence[] entries;
    private CharSequence[] entryValues;
    Set<String> newValues = new HashSet<>();
    boolean preferenceChanged;
    
    @NonNull
    public static M3MultiSelectListPreferenceDialogFragment newInstance(@NonNull String key) {
        final M3MultiSelectListPreferenceDialogFragment fragment = new M3MultiSelectListPreferenceDialogFragment();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            final MultiSelectListPreference preference = getListPreference();
            if (preference.getEntries() == null || preference.getEntryValues() == null) {
                throw new IllegalStateException("MultiSelectListPreference requires an entries array and " + "an entryValues array.");
            }
            newValues.clear();
            newValues.addAll(preference.getValues());
            preferenceChanged = false;
            entries = preference.getEntries();
            entryValues = preference.getEntryValues();
        } else {
            newValues.clear();
            newValues.addAll(savedInstanceState.getStringArrayList(SAVE_STATE_VALUES));
            preferenceChanged = savedInstanceState.getBoolean(SAVE_STATE_CHANGED, false);
            entries = savedInstanceState.getCharSequenceArray(SAVE_STATE_ENTRIES);
            entryValues = savedInstanceState.getCharSequenceArray(SAVE_STATE_ENTRY_VALUES);
        }
    }
    
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(SAVE_STATE_VALUES, new ArrayList<>(newValues));
        outState.putBoolean(SAVE_STATE_CHANGED, preferenceChanged);
        outState.putCharSequenceArray(SAVE_STATE_ENTRIES, entries);
        outState.putCharSequenceArray(SAVE_STATE_ENTRY_VALUES, entryValues);
    }
    
    private MultiSelectListPreference getListPreference() {
        return (MultiSelectListPreference) getPreference();
    }
    
    @Override
    protected void onPrepareDialogBuilder(@NonNull MaterialAlertDialogBuilder builder) {
        super.onPrepareDialogBuilder(builder);
        final int entryCount = entryValues.length;
        final boolean[] checkedItems = new boolean[entryCount];
        for (int i = 0; i < entryCount; i++) {
            checkedItems[i] = newValues.contains(entryValues[i].toString());
        }
        builder.setMultiChoiceItems(entries, checkedItems, (dialog, which, isChecked) -> {
            if (isChecked) {
                preferenceChanged |= newValues.add(entryValues[which].toString());
            } else {
                preferenceChanged |= newValues.remove(entryValues[which].toString());
            }
        });
    }
    
    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult && preferenceChanged) {
            final MultiSelectListPreference preference = getListPreference();
            if (preference.callChangeListener(newValues)) {
                preference.setValues(newValues);
            }
        }
        preferenceChanged = false;
    }
}