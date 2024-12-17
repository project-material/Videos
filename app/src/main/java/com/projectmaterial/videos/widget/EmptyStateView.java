package com.projectmaterial.videos.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.projectmaterial.videos.R;

public class EmptyStateView extends LinearLayout {

    private ImageView emptyIcon;
    private TextView emptyTitle;
    private TextView emptyMessage;

    public EmptyStateView(Context context) {
        this(context, null);
    }

    public EmptyStateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyStateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        LayoutInflater.from(context).inflate(R.layout.empty_state_view, this, true);

        emptyIcon = findViewById(R.id.empty_icon);
        emptyTitle = findViewById(R.id.empty_title);
        emptyMessage = findViewById(R.id.empty_message);
    }

    public void setIcon(@DrawableRes int drawableRes) {
        emptyIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), drawableRes));
    }

    public void setIconBackground(@DrawableRes int backgroundRes) {
        emptyIcon.setBackgroundResource(backgroundRes);
    }

    public void setTitle(String title) {
        emptyTitle.setText(title);
    }

    public void setTitle(@StringRes int titleRes) {
        emptyTitle.setText(titleRes);
    }

    public void setMessage(String message) {
        emptyMessage.setText(message);
    }

    public void setMessage(@StringRes int messageRes) {
        emptyMessage.setText(messageRes);
    }
}