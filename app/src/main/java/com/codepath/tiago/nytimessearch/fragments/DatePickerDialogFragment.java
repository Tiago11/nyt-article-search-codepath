package com.codepath.tiago.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Window;

import java.util.Calendar;

/**
 * Created by tiago on 9/24/17.
 */

public class DatePickerDialogFragment extends DialogFragment {

    public DatePickerDialogFragment() {
        // DialogFragment needs an empty constructor.
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // Use the current date as the default value for the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Parent fragment needs to implement this interface.
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getTargetFragment();

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(getContext(), listener, year, month, day);
    }

}
