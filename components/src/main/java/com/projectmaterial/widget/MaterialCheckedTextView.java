package com.projectmaterial.widget;

import static com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.TintTypedArray;

import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

public class MaterialCheckedTextView extends AppCompatCheckedTextView {
    
    private static final int DEF_STYLE_RES = R.style.Widget_ProjectX_MaterialCheckedTextView;
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};
    private static final int[] DISABLED_STATE_SET = {-android.R.attr.state_enabled};
    private Drawable icon;
    
    public MaterialCheckedTextView(@NonNull Context context) {
        this(context, null);
    }
    
    public MaterialCheckedTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.materialCheckedTextViewStyle);
    }
    
    public MaterialCheckedTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(wrap(context, attrs, defStyleAttr, DEF_STYLE_RES), attrs, defStyleAttr);
        // Ensure we are using the correctly themed context rather than the context that was passed in.
        context = getContext();
        
        // Custom attributes
        TintTypedArray a =
            ThemeEnforcement.obtainTintedStyledAttributes(
                context, attrs, R.styleable.MaterialCheckedTextView, defStyleAttr, DEF_STYLE_RES);
        
        final ColorStateList iconTint;
        if (a.hasValue(R.styleable.MaterialCheckedTextView_iconTint)) {
            iconTint = a.getColorStateList(R.styleable.MaterialCheckedTextView_iconTint);
        } else {
            iconTint = createDefaultColorStateList(android.R.attr.textColorSecondary);
        }
        
        Drawable background = a.getDrawable(R.styleable.MaterialCheckedTextView_background);
        // Set a shaped itemBackground if itemBackground hasn't been set and there is a shape
        // appearance.
        if (background == null && hasShapeAppearance(a)) {
            background = createDefaultBackground(a);

            ColorStateList rippleColor =
                MaterialResources.getColorStateList(
                    context, a, R.styleable.MaterialCheckedTextView_rippleColor);

            // Use a ripple matching the item's shape as the foreground if a ripple color is set.
            // Otherwise the selectableItemBackground foreground from the item layout will be used.
            if (rippleColor != null) {
                Drawable rippleMask = createDefaultDrawable(a, null);
                RippleDrawable ripple =
                    new RippleDrawable(
                        RippleUtils.sanitizeRippleDrawableColor(rippleColor), null, rippleMask);
                setForeground(ripple.getConstantState().newDrawable());
            }
        }
        
        final int iconPadding =
            a.getDimensionPixelSize(R.styleable.MaterialCheckedTextView_iconPadding, 0);
        
        setIconTintList(iconTint);
        setBackground(background.getConstantState().newDrawable());
        setIconPadding(iconPadding);
        
        a.recycle();
        
        init(context);
    }
    
    @Override
    public void setChecked(boolean isChecked) {
        super.setChecked(isChecked);
        setIconDirection();
    }
    
    /**
     * Initializes the view by setting the default icon and updating its position.
     *
     * @param context The context used to access resources.
     */
    private void init(@NonNull Context context) {
        icon = AppCompatResources.getDrawable(context, R.drawable.quantum_ic_check_vd_theme_24);
        setIconDirection();
    }
    
    /**
     * Sets the direction of the check mark icon based on the layout direction and check state.
     * The icon is placed on the start (left) or end (right) side depending on the RTL layout direction.
     */
    private void setIconDirection() {
        Drawable left = null, right = null;
        
        // Determine the position of the check mark drawable if checked
        if (isChecked()) {
            if (getLayoutDirection() == LAYOUT_DIRECTION_RTL) {
                // In RTL layout, place the icon on the right
                right = icon;
            } else {
                // In LTR layout, place the icon on the left
                left = icon;
            }
        }
        
        // Set the drawable to the appropriate position
        setCompoundDrawablesWithIntrinsicBounds(left, null, right, null);
    }
    
    /**
     * Sets the padding (in pixels) between the icon and the text.
     *
     * @param padding The padding value in pixels.
     * @attr ref R.styleable#MaterialCheckedTextView_iconPadding
     */
    public void setIconPadding(@Dimension int padding) {
        setCompoundDrawablePadding(padding);
    }
    
    /**
     * Applies a tint to the icon.
     *
     * @param tint The color state list to use as the tint.
     * @attr ref R.styleable#MaterialCheckedTextView_iconTint
     */
    public void setIconTintList(@Nullable ColorStateList tint) {
        setCompoundDrawableTintList(tint);
    }
                
    @NonNull
    private Drawable createDefaultBackground(@NonNull TintTypedArray a) {
        ColorStateList fillColor = MaterialResources.getColorStateList(getContext(), a, R.styleable.MaterialCheckedTextView_backgroundTint);
        return createDefaultDrawable(a, fillColor);
    }
    
    @Nullable
    private ColorStateList createDefaultColorStateList(int baseColorThemeAttr) {
        final TypedValue value = new TypedValue();
        if (!getContext().getTheme().resolveAttribute(baseColorThemeAttr, value, true)) {
            return null;
        }
        ColorStateList baseColor = AppCompatResources.getColorStateList(getContext(), value.resourceId);
        if (!getContext().getTheme().resolveAttribute(R.attr.colorPrimary, value, true)) {
            return null;
        }
        int colorPrimary = value.data;
        int defaultColor = baseColor.getDefaultColor();
        return new ColorStateList(
            new int[][] {DISABLED_STATE_SET, CHECKED_STATE_SET, EMPTY_STATE_SET},
            new int[] {
                baseColor.getColorForState(DISABLED_STATE_SET, defaultColor), colorPrimary, defaultColor
            });
    }
    
    @NonNull
    private Drawable createDefaultDrawable(@NonNull TintTypedArray a, @Nullable ColorStateList fillColor) {
        int shapeAppearanceResId = a.getResourceId(R.styleable.MaterialCheckedTextView_shapeAppearance, 0);
        int shapeAppearanceOverlayResId = a.getResourceId(R.styleable.MaterialCheckedTextView_shapeAppearanceOverlay, 0);
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(ShapeAppearanceModel.builder(getContext(), shapeAppearanceResId, shapeAppearanceOverlayResId).build());
        materialShapeDrawable.setFillColor(fillColor);

        int insetLeft = a.getDimensionPixelSize(R.styleable.MaterialCheckedTextView_backgroundInsetStart, 0);
        int insetTop = a.getDimensionPixelSize(R.styleable.MaterialCheckedTextView_backgroundInsetTop, 0);
        int insetRight = a.getDimensionPixelSize(R.styleable.MaterialCheckedTextView_backgroundInsetEnd, 0);
        int insetBottom = a.getDimensionPixelSize(R.styleable.MaterialCheckedTextView_backgroundInsetBottom, 0);
        return new InsetDrawable(materialShapeDrawable, insetLeft, insetTop, insetRight, insetBottom);
    }
    
    private boolean hasShapeAppearance(@NonNull TintTypedArray a) {
        return a.hasValue(R.styleable.MaterialCheckedTextView_shapeAppearance)
            || a.hasValue(R.styleable.MaterialCheckedTextView_shapeAppearanceOverlay);
    }
}