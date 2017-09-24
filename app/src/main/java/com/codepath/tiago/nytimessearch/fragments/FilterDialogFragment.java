package com.codepath.tiago.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;

import com.codepath.tiago.nytimessearch.R;

import java.util.Calendar;

/**
 * Created by tiago on 9/23/17.
 */

public class FilterDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private EditText etDate;

    public FilterDialogFragment() {
        // Empty constructor required for DialogFragment.
        // We use `newInstance` to instantiate.
    }

    public static FilterDialogFragment newInstance() {
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filters, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etDate = (EditText) view.findViewById(R.id.etDate);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // Request a window without the title.
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void showDatePickerDialog(View v) {
        FragmentManager fm = getFragmentManager();
        DatePickerDialogFragment frag = new DatePickerDialogFragment();
        frag.setTargetFragment(FilterDialogFragment.this, 300);
        frag.show(fm, "datepicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance.
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        etDate.setText(c.getTime().toString());
    }

}


























