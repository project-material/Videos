package com.projectmaterial.preference;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceDialogFragmentCompat;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public abstract class M3PreferenceDialogFragment extends PreferenceDialogFragmentCompat {
    
    private int whichButtonClicked;
    
    @Override
    @NonNull
    public AlertDialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        whichButtonClicked = DialogInterface.BUTTON_NEGATIVE;
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext(), R.style.ThemeOverlay_ProjectX_MaterialAlertDialog_Centered)
                .setTitle(getPreference().getDialogTitle())
                .setIcon(getPreference().getDialogIcon())
                .setPositiveButton(getPreference().getPositiveButtonText(), this)
                .setNegativeButton(getPreference().getNegativeButtonText(), this);
        View contentView = onCreateDialogView(requireContext());
        if (contentView != null) {
            onBindDialogView(contentView);
            builder.setView(contentView);
        } else {
            builder.setMessage(getPreference().getDialogMessage());
        }
        onPrepareDialogBuilder(builder);
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            ListView listView = dialog.getListView();
            listView.setSelector(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        });
        if (needInputMethod()) {
            requestInputMethod(dialog);
        }
        return dialog;
    }
    
    protected void onPrepareDialogBuilder(@NonNull MaterialAlertDialogBuilder builder) {}
    
    @Override
    protected boolean needInputMethod() {
        return false;
    }
    
    private void requestInputMethod(@NonNull AlertDialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            View decorView = window.getDecorView();
            WindowInsetsController windowInsetsController = decorView.getWindowInsetsController();
            if (windowInsetsController != null) {
                windowInsetsController.show(WindowInsets.Type.ime());
            }
        }
    }
    
    @Override
    public void onClick(@NonNull DialogInterface dialog, int which) {
        whichButtonClicked = which;
    }
    
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        onDialogClosed(whichButtonClicked == DialogInterface.BUTTON_POSITIVE);
    }
}