package com.projectmaterial.videos.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.projectmaterial.videos.R;
import com.projectmaterial.videos.activity.base.BaseSettingsActivity;
import com.projectmaterial.videos.fragment.SettingsFragment;

public class SettingsActivity extends BaseSettingsActivity {

    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsFragment = new SettingsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, settingsFragment)
                .commitNow();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTitle(R.string.settings);
    }

    @Override
    protected void restart() {
        if (settingsFragment.isPreferenceChanged()) {
            try {
                finish();
                overrideTransition();
                startActivity(getIntent());
                overrideTransition();
            } catch (Throwable e) {
                e.printStackTrace();
                recreate();
            }
        }
    }
}