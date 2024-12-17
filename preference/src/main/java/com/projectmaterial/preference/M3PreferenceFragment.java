package com.projectmaterial.preference;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.RecyclerView;

public abstract class M3PreferenceFragment extends PreferenceFragmentCompat {
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        if (recyclerView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(recyclerView, (v, insets) -> {
                Insets systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(0, 0, 0, systemInsets.bottom);
                return WindowInsetsCompat.CONSUMED;
            });
        }
    }
    
    @Override
    @SuppressWarnings("deprecation")
    public void onDisplayPreferenceDialog(@NonNull Preference preference) {
        final DialogFragment fragment;
        if (preference instanceof EditTextPreference) {
            fragment = M3EditTextPreferenceDialogFragment.newInstance(preference.getKey());
            fragment.setTargetFragment(this, 0);
            fragment.show(getParentFragmentManager(), "androidx.preference.PreferenceFragment.DIALOG");
        } else if (preference instanceof ListPreference) {
            fragment = M3ListPreferenceDialogFragment.newInstance(preference.getKey());
            fragment.setTargetFragment(this, 0);
            fragment.show(getParentFragmentManager(), "androidx.preference.PreferenceFragment.DIALOG");
        } else if (preference instanceof MultiSelectListPreference) {
            fragment = M3MultiSelectListPreferenceDialogFragment.newInstance(preference.getKey());
            fragment.setTargetFragment(this, 0);
            fragment.show(getParentFragmentManager(), "androidx.preference.PreferenceFragment.DIALOG");
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}