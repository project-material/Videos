package com.projectmaterial.preference;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class M3EditTextPreferenceDialogFragment extends M3PreferenceDialogFragment {
    
    private static final String SAVE_STATE_TEXT = "M3EditTextPreferenceDialogFragment.text";
    private static final int SHOW_REQUEST_TIMEOUT = 1000;
    private CharSequence text;
    private EditText editText;
    private final Runnable showSoftInputRunnable = this::scheduleShowSoftInputInner;
    private long showRequestTime = -1;
    
    @NonNull
    public static M3EditTextPreferenceDialogFragment newInstance(@NonNull String key) {
        final M3EditTextPreferenceDialogFragment fragment = new M3EditTextPreferenceDialogFragment();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            text = getEditTextPreference().getText();
        } else {
            text = savedInstanceState.getCharSequence(SAVE_STATE_TEXT);
        }
    }
    
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(SAVE_STATE_TEXT, text);
    }
    
    @Override
    protected void onBindDialogView(@NonNull View view) {
        super.onBindDialogView(view);
        editText = view.findViewById(android.R.id.edit);
        if (editText == null) {
            throw new IllegalStateException("Dialog view must contain an EditText with id" + " @android:id/edit");
        }
        editText.requestFocus();
        editText.setText(text);
        editText.setSelection(editText.getText().length());
        if (getEditTextPreference().getOnBindEditTextListener() != null) {
            getEditTextPreference().getOnBindEditTextListener().onBindEditText(editText);
        }
    }
    
    private M3EditTextPreference getEditTextPreference() {
        return (M3EditTextPreference) getPreference();
    }
    
    @Override
    protected boolean needInputMethod() {
        return true;
    }
    
    private boolean hasPendingShowSoftInputRequest() {
        return (showRequestTime != -1 && ((showRequestTime + SHOW_REQUEST_TIMEOUT) > SystemClock.currentThreadTimeMillis()));
    }
    
    private void setPendingShowSoftInputRequest(boolean pendingShowSoftInputRequest) {
        showRequestTime = pendingShowSoftInputRequest ? SystemClock.currentThreadTimeMillis() : -1;
    }
    
    @Override
    protected void scheduleShowSoftInput() {
        setPendingShowSoftInputRequest(true);
        scheduleShowSoftInputInner();
    }
    
    void scheduleShowSoftInputInner() {
        if (hasPendingShowSoftInputRequest()) {
            if (editText == null || !editText.isFocused()) {
                setPendingShowSoftInputRequest(false);
                return;
            }
            final InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.showSoftInput(editText, 0)) {
                setPendingShowSoftInputRequest(false);
            } else {
                editText.removeCallbacks(showSoftInputRunnable);
                editText.postDelayed(showSoftInputRunnable, 50);
            }
        }
    }
    
    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String value = editText.getText().toString();
            final M3EditTextPreference preference = getEditTextPreference();
            if (preference.callChangeListener(value)) {
                preference.setText(value);
            }
        }
    }
}
