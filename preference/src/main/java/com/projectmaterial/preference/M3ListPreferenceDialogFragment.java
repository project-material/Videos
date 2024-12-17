package com.projectmaterial.preference;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class M3ListPreferenceDialogFragment extends M3PreferenceDialogFragment {
    
    private static final String SAVE_STATE_INDEX = "M3ListPreferenceDialogFragment.index";
    private static final String SAVE_STATE_ENTRIES = "M3ListPreferenceDialogFragment.entries";
    private static final String SAVE_STATE_ENTRY_VALUES = "M3ListPreferenceDialogFragment.entryValues";
    private CharSequence[] entries;
    private CharSequence[] entryValues;
    int clickedDialogEntryIndex;
    
    @NonNull
    public static M3ListPreferenceDialogFragment newInstance(@NonNull String key) {
        final M3ListPreferenceDialogFragment fragment = new M3ListPreferenceDialogFragment();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            final ListPreference preference = getListPreference();
            if (preference.getEntries() == null || preference.getEntryValues() == null) {
                throw new IllegalStateException("ListPreference requires an entries array and an entryValues array.");
            }
            clickedDialogEntryIndex = preference.findIndexOfValue(preference.getValue());
            entries = preference.getEntries();
            entryValues = preference.getEntryValues();
        } else {
            clickedDialogEntryIndex = savedInstanceState.getInt(SAVE_STATE_INDEX, 0);
            entries = savedInstanceState.getCharSequenceArray(SAVE_STATE_ENTRIES);
            entryValues = savedInstanceState.getCharSequenceArray(SAVE_STATE_ENTRY_VALUES);
        }
    }
    
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_STATE_INDEX, clickedDialogEntryIndex);
        outState.putCharSequenceArray(SAVE_STATE_ENTRIES, entries);
        outState.putCharSequenceArray(SAVE_STATE_ENTRY_VALUES, entryValues);
    }
    
    private ListPreference getListPreference() {
        return (ListPreference) getPreference();
    }
    
    @Override
    protected void onPrepareDialogBuilder(@NonNull MaterialAlertDialogBuilder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setSingleChoiceItems(entries, clickedDialogEntryIndex, (dialog, which) -> {
            clickedDialogEntryIndex = which;
            onClick(dialog, DialogInterface.BUTTON_POSITIVE);
            dialog.dismiss();
        });
        builder.setPositiveButton(null, null);
    }
    
    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult && clickedDialogEntryIndex >= 0) {
            String value = entryValues[clickedDialogEntryIndex].toString();
            final ListPreference preference = getListPreference();
            if (preference.callChangeListener(value)) {
                preference.setValue(value);
            }
        }
    }
}