package com.codepath.tiago.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import com.codepath.tiago.nytimessearch.R;

import java.lang.reflect.Field;
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

        // Use the current date as the default value for the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Parent fragment needs to implement this interface.
        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getTargetFragment();

        // Create a new instance of DatePickerDialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), 0, listener, year, month, day);
        // Remove the title in the DatePickerDialog.
        datePickerDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        colorizeDatePickerDialog(datePickerDialog);

        return datePickerDialog;
    }

    public static void colorizeDatePickerDialog(DatePickerDialog datePickerDialog) {
        DatePicker mDatePickerInstance = null;
        try {
            Field mDatePickerField = datePickerDialog.getClass().getDeclaredField("mDatePicker");
            mDatePickerField.setAccessible(true);
            mDatePickerInstance = (DatePicker) mDatePickerField.get(datePickerDialog);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (mDatePickerInstance != null) {
            Resources system = Resources.getSystem();
            int dayId = system.getIdentifier("day", "id", "android");
            int monthId = system.getIdentifier("month", "id", "android");
            int yearId = system.getIdentifier("year", "id", "android");

            NumberPicker dayPicker = (NumberPicker) mDatePickerInstance.findViewById(dayId);
            NumberPicker monthPicker = (NumberPicker) mDatePickerInstance.findViewById(monthId);
            NumberPicker yearPicker = (NumberPicker) mDatePickerInstance.findViewById(yearId);

            setDividerColor(dayPicker);
            setDividerColor(monthPicker);
            setDividerColor(yearPicker);
        }
    }

    private static void setDividerColor(NumberPicker picker) {
        if (picker == null)
            return;

        final int count = picker.getChildCount();
        for (int i = 0; i < count; i++) {
            try {
                Field dividerField = picker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(picker.getResources().getColor(R.color.colorAccent));
                dividerField.set(picker, colorDrawable);
                picker.invalidate();
            } catch (Exception e) {
                Log.w("setDividerColor", e);
            }
        }
    }

}
