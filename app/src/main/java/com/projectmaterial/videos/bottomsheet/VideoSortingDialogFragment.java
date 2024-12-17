package com.projectmaterial.videos.bottomsheet;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import com.projectmaterial.videos.R;
import com.projectmaterial.videos.adapter.base.BaseVideoAdapter;
import com.projectmaterial.videos.util.SortingCriteria;
import com.projectmaterial.videos.util.SortingOrder;

public class VideoSortingDialogFragment extends BottomSheetDialogFragment {
    
    private BaseVideoAdapter adapter;
    private MaterialButton buttonSortByName;
    private MaterialButton buttonSortByDuration;
    private MaterialButton buttonSortByDate;
    private MaterialButton buttonSortBySize;
    private MaterialButton buttonSortAscending;
    private MaterialButton buttonSortDescending;
    private MaterialButtonToggleGroup toggleGroupCriteria;
    private MaterialButtonToggleGroup toggleGroupOrder;
    
    public static final int[] ASCENDING_LABEL = {
        R.string.sort_by_order_a_z,
        R.string.sort_by_order_shortest,
        R.string.sort_by_order_oldest,
        R.string.sort_by_order_smallest
    };
    
    public static final int[] DESCENDING_LABEL = {
        R.string.sort_by_order_z_a,
        R.string.sort_by_order_longest,
        R.string.sort_by_order_newest,
        R.string.sort_by_order_largest
    };
    
    public VideoSortingDialogFragment(@NonNull BaseVideoAdapter adapter) {
        this.adapter = adapter;
    }
    
    @Override
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.bottom_sheet_sorting_video, container, false);
        
        toggleGroupCriteria = contentView.findViewById(R.id.toggle_group_criteria);
        toggleGroupCriteria.setSelectionRequired(true);
        toggleGroupCriteria.setSingleSelection(true);
        
        buttonSortByName = contentView.findViewById(R.id.btn_sort_by_name);
        configureCriteriaClickListener(buttonSortByName, SortingCriteria.Video.VIDEO_NAME);
        buttonSortByDuration = contentView.findViewById(R.id.btn_sort_by_duration);
        configureCriteriaClickListener(buttonSortByDuration, SortingCriteria.Video.VIDEO_DURATION);
        buttonSortByDate = contentView.findViewById(R.id.btn_sort_by_date);
        configureCriteriaClickListener(buttonSortByDate, SortingCriteria.Video.VIDEO_DATE);
        buttonSortBySize = contentView.findViewById(R.id.btn_sort_by_size);
        configureCriteriaClickListener(buttonSortBySize, SortingCriteria.Video.VIDEO_SIZE);
        
        toggleGroupOrder = contentView.findViewById(R.id.toggle_group_order);
        toggleGroupOrder.setSelectionRequired(true);
        toggleGroupOrder.setSingleSelection(true);
        
        buttonSortAscending = contentView.findViewById(R.id.btn_sort_asc);
        configureOrderClickListener(buttonSortAscending, SortingOrder.ASCENDING);
        buttonSortDescending = contentView.findViewById(R.id.btn_sort_desc);
        configureOrderClickListener(buttonSortDescending, SortingOrder.DESCENDING);
        
        switch (adapter.getSortingCriteria()) {
            case VIDEO_NAME:
                buttonSortByName.setChecked(true);
                updateSortButtonLabels(ASCENDING_LABEL[0], DESCENDING_LABEL[0]);
                break;
            case VIDEO_DURATION:
                buttonSortByDuration.setChecked(true);
                updateSortButtonLabels(ASCENDING_LABEL[1], DESCENDING_LABEL[1]);
                break;
            case VIDEO_DATE:
                buttonSortByDate.setChecked(true);
                updateSortButtonLabels(ASCENDING_LABEL[2], DESCENDING_LABEL[2]);
                break;
            case VIDEO_SIZE:
                buttonSortBySize.setChecked(true);
                updateSortButtonLabels(ASCENDING_LABEL[3], DESCENDING_LABEL[3]);
                break;
        }
        
        switch (adapter.getSortingOrder()) {
            case ASCENDING:
                buttonSortAscending.setChecked(true);
                break;
            case DESCENDING:
                buttonSortDescending.setChecked(true);
                break;
        }
        
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
        
        return contentView;
    }
    
    /**
     * Configures a click listener for a button to update sorting criteria.
     *
     * @param button  The button to attach the click listener to.
     * @param criteria The sorting criteria to apply when clicked.
     */
    @UiThread
    private void configureCriteriaClickListener(@NonNull MaterialButton button, @NonNull SortingCriteria.Video criteria) {
        button.setOnClickListener(view -> {
            adapter.updateSortingCriteria(criteria);
            dismiss();
        });
    }
    
    /**
     * Configures a click listener for a button to update sorting order.
     *
     * @param button The button to attach the click listener to.
     * @param order  The sorting order to apply when clicked.
     */
    @UiThread
    private void configureOrderClickListener(@NonNull MaterialButton button, @NonNull SortingOrder order) {
        button.setOnClickListener(view -> {
            adapter.updateSortingOrder(order);
        });
    }
    
    /**
     * Updates the labels for the ascending and descending sort buttons.
     *
     * @param ascLabel  The resource ID for the ascending sort button label.
     * @param descLabel The resource ID for the descending sort button label.
     */
    @UiThread
    private void updateSortButtonLabels(@StringRes int ascLabel, @StringRes int descLabel) {
        buttonSortAscending.setText(ascLabel);
        buttonSortDescending.setText(descLabel);
    }
}