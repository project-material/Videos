package com.projectmaterial.preference;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import java.net.URISyntaxException;

public class M3FooterPreference extends Preference {
    
    private static final String TAG = "FooterPreference";
    private static final String KEY_FOOTER = "footer_preference";
    private static final String INTENT_URL_PREFIX = "intent:";
    private static final int ORDER_FOOTER = Integer.MAX_VALUE - 1;
    
    public M3FooterPreference(@NonNull Context context) {
        this(context, null);
    }
    
    public M3FooterPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, R.attr.footerPreferenceStyle);
        setLayoutResource(R.layout.m3_preference_footer);
        if (getIcon() == null) {
            setIcon(R.drawable.quantum_ic_info_vd_theme_24);
        }
        setOrder(ORDER_FOOTER);
        if (TextUtils.isEmpty(getKey())) {
            setKey(KEY_FOOTER);
        }
        setSelectable(false);
    }
    
    private void linkifyTitle(@NonNull TextView title) {
        final CharSequence text = getTitle();
        if (!(text instanceof Spanned)) {
            return;
        }
        final ClickableSpan[] spans = ((Spanned) text).getSpans(0, text.length(), ClickableSpan.class);
        if (spans.length == 0) {
            return;
        }
        SpannableString spannable = new SpannableString(text);
        for (ClickableSpan clickable : spans) {
            if (!(clickable instanceof URLSpan)) {
                continue;
            }
            final URLSpan urlSpan = (URLSpan) clickable;
            final String url = urlSpan.getURL();
            if (url == null || !url.startsWith(INTENT_URL_PREFIX)) {
                continue;
            }
            final int start = spannable.getSpanStart(urlSpan);
            final int end = spannable.getSpanEnd(urlSpan);
            spannable.removeSpan(urlSpan);
            try {
                final Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                final ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View textView) {
                        getContext().startActivity(intent);
                    }
                };
                spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (URISyntaxException e) {
                Log.e(TAG, "Invalid URI " + url, e);
            }
        }
        title.setText(spannable);
        title.setMovementMethod(LinkMovementMethod.getInstance());
    }
    
    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        TextView title = holder.itemView.findViewById(android.R.id.title);
        if (title != null) {
            linkifyTitle(title);
        }
    }
    
    @Override
    public void setSummary(@Nullable CharSequence summary) {
        setTitle(summary);
    }
    
    @Override
    public void setSummary(int summaryResId) {
        setTitle(summaryResId);
    }
    
    @Override
    @Nullable
    public CharSequence getSummary() {
        return getTitle();
    }
}