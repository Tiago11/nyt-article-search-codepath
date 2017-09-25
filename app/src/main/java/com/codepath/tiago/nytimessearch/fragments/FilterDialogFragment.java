package com.codepath.tiago.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.tiago.nytimessearch.R;
import com.codepath.tiago.nytimessearch.models.Filter;
import com.codepath.tiago.nytimessearch.utils.DateFormatter;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tiago on 9/23/17.
 */

public class FilterDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private EditText etDate;
    private Spinner sSort;
    private CheckBox cbArts;
    private CheckBox cbFashionStyle;
    private CheckBox cbSports;
    private Button btnReset;
    private Button btnSave;

    private Date mDate;

    public FilterDialogFragment() {
        // Empty constructor required for DialogFragment.
        // We use `newInstance` to instantiate.
    }

    public static FilterDialogFragment newInstance(Filter filter) {
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("filter", Parcels.wrap(filter));
        frag.setArguments(args);
        return frag;
    }

    // Interface that defines the listener used to pass data back to the activity.
    public interface FilterDialogListener {
        void onFinishFilterDialog(Filter filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_filters, container);
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_filters, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get references to the dialog views and fields.
        setupViews(view);

        // Get the filter settings from the activity and set it into the views.
        Filter filter = (Filter) Parcels.unwrap(getArguments().getParcelable("filter"));
        setFilterInfoIntoViews(filter);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // Request a window without the title.
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    /*
     * Starts the datePickerDialog.
     */
    public void showDatePickerDialog(View v) {
        FragmentManager fm = getFragmentManager();
        DatePickerDialogFragment frag = new DatePickerDialogFragment();
        frag.setTargetFragment(FilterDialogFragment.this, 300);
        frag.show(fm, "DatePickerDialogTheme");
    }

    /*
     * Get the date from the datePickerDialog and set it into the textField.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance.
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        mDate = c.getTime();
        etDate.setText(DateFormatter.FormatDateToString(c.getTime()));
    }

    /*
     * Get references for the views and set listeners.
     */
    private void setupViews(View view) {
        etDate = (EditText) view.findViewById(R.id.etDate);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        sSort = (Spinner) view.findViewById(R.id.sSort);
        cbArts = (CheckBox) view.findViewById(R.id.cbArts);
        cbFashionStyle = (CheckBox) view.findViewById(R.id.cbFashionStyle);
        cbSports = (CheckBox) view.findViewById(R.id.cbSports);
        btnReset = (Button) view.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetFilter(view);
            }
        });
        btnReset.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFilter(view);
            }
        });
        btnSave.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        btnSave.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

    }

    /*
     * Create the Filter object from the views information and send it back to the activity.
     */
    public void saveFilter(View view) {
        Filter filter = getFilterFromViews();

        FilterDialogListener listener = (FilterDialogListener) getActivity();
        listener.onFinishFilterDialog(filter);

        dismiss();
    }

    /*
     * Send an empty (null) filter back to the activity to indicate a reset on the filter settings.
     */
    public void resetFilter(View view) {
        Filter filter = null;

        FilterDialogListener listener = (FilterDialogListener) getActivity();
        listener.onFinishFilterDialog(filter);

        dismiss();
    }

    /*
     * Create a Filter object from the views.
     */
    private Filter getFilterFromViews() {

        // Get the sort order.
        String sort = sSort.getSelectedItem().toString();
        Filter.SortValues sortOrder = null;
        if ("Oldest".equals(sort)) {
            sortOrder = Filter.SortValues.OLDEST;
        } else if ("Newest".equals(sort)) {
            sortOrder = Filter.SortValues.NEWEST;
        }

        // Get the news desk values.
        Set<Filter.NewsDeskValues> newsDeskValuesSet = new HashSet<>();
        if (cbArts.isChecked()) {
            newsDeskValuesSet.add(Filter.NewsDeskValues.ARTS);
        }
        if (cbFashionStyle.isChecked()) {
            newsDeskValuesSet.add(Filter.NewsDeskValues.FASHION_STYLE);
        }
        if (cbSports.isChecked()) {
            newsDeskValuesSet.add(Filter.NewsDeskValues.SPORTS);
        }

        // Create the filter object.
        Filter filter = new Filter();
        filter.setBeginDate(mDate);
        filter.setSortOrder(sortOrder);
        filter.setNewsDeskValues(newsDeskValuesSet);

        return filter;
    }

    /*
     * Set information into the views according to the information in the filter object.
     */
    private void setFilterInfoIntoViews(Filter filter) {
        if (filter != null) {
            etDate.setText(DateFormatter.FormatDateToString(filter.getBeginDate()));
            mDate = filter.getBeginDate();

            // Set spinner.
            if (filter.getSortOrder() == Filter.SortValues.OLDEST) {
                sSort.setSelection(0); // Index of "Oldest".
            } else if (filter.getSortOrder() == Filter.SortValues.NEWEST) {
                sSort.setSelection(1); // Index of "Newest".
            }

            // Set checkboxes.
            if (filter.hasNewsDeskValue(Filter.NewsDeskValues.ARTS)) {
                cbArts.setChecked(true);
            } else {
                cbArts.setChecked(false);
            }

            if (filter.hasNewsDeskValue(Filter.NewsDeskValues.FASHION_STYLE)) {
                cbFashionStyle.setChecked(true);
            } else {
                cbFashionStyle.setChecked(false);
            }

            if (filter.hasNewsDeskValue(Filter.NewsDeskValues.SPORTS)) {
                cbSports.setChecked(true);
            } else {
                cbSports.setChecked(false);
            }
        }
    }
}
