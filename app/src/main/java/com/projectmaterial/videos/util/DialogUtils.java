package com.projectmaterial.videos.util;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.projectmaterial.videos.R;
import com.projectmaterial.videos.activity.CollectionActivity;
import com.projectmaterial.videos.activity.VideoLibraryActivity;
import com.projectmaterial.videos.app.VideoApplication;
import com.projectmaterial.videos.database.Video;

public class DialogUtils {
    
    public static void showDeleteConfirmationDialog(@NonNull Context context, @NonNull Video video) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle(R.string.dialog_delete_title);
        builder.setMessage(R.string.dialog_delete_message);
        builder.setNegativeButton(R.string.button_cancel, (dialog, which) -> {
            dialog.cancel();
        });
        builder.setPositiveButton(R.string.button_delete, (dialog, which) -> {
            VideoOptionUtils.deleteVideo(context, video);
            dialog.dismiss();
        });
        builder.show();
    }
    
    public static void showRenameVideoDialog(@NonNull Context context, @NonNull Video video) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_ProjectX_MaterialAlertDialog_Centered);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_rename, null);
        
        TextInputEditText input = contentView.findViewById(R.id.text_input_edit_text);
        TextInputLayout inputLayout = contentView.findViewById(R.id.text_input_layout);
        
        String data = video.getData();
        String currentName = video.getDisplayName();
        
        input.setText(currentName);
        
        builder.setTitle(R.string.dialog_rename_title);
        builder.setView(contentView);
        builder.setNegativeButton(R.string.button_cancel, (dialog, which) -> dialog.cancel());
        builder.setPositiveButton(R.string.button_rename, null);
        
        AlertDialog dialog = builder.create();
        
        dialog.setOnShowListener(dialogInterface -> {
            Button button1 = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button1.setEnabled(false);
            button1.setOnClickListener(view -> {
                String newName = input.getText().toString().trim();
                if (!newName.isEmpty() && isValid(newName)) {
                    String newData = data.replace(currentName, newName);
                    if (VideoOptionUtils.renameVideo(data, newData)) {
                        showSnackbar(context.getString(R.string.snackbar_rename_success));
                    } else {
                        showSnackbar(context.getString(R.string.snackbar_rename_error));
                    }
                    dialog.dismiss();
                } else {
                    String inputEmpty = context.getString(R.string.dialog_rename_error_empty_input);
                    String inputInvalid = context.getString(R.string.dialog_rename_error_invalid_characters);
                    inputLayout.setError(newName.isEmpty() ? inputEmpty : inputInvalid);
                }
            });
            input.addTextChangedListener(getTextWatcher(button1, currentName));
        });
        dialog.show();
    }
    
    private static boolean isValid(@NonNull String name) {
    	String[] invalidChars = {"?", ":", "*", "|", "/", "\\", "<", ">"};
        for (String invalidChar : invalidChars) {
            if (name.contains(invalidChar)) {
                return false;
            }
        }
        return true;
    }
    
    private static TextWatcher getTextWatcher(@NonNull Button button, @NonNull String name) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newName = s.toString().trim();
                boolean nameChanged = !newName.equals(name);
                button.setEnabled(nameChanged);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
    }
    
    private static void showSnackbar(@NonNull String message) {
        Activity currentActivity = VideoApplication.getInstance().getCurrentActivity();
        if (currentActivity instanceof VideoLibraryActivity) {
            ((VideoLibraryActivity) currentActivity).showSnackbar(message);
        } else if (currentActivity instanceof CollectionActivity) {
            ((CollectionActivity) currentActivity).showSnackbar(message);
        }
    }
}