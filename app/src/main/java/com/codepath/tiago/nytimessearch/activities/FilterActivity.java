package com.codepath.tiago.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.codepath.tiago.nytimessearch.R;
import com.codepath.tiago.nytimessearch.models.Filter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class FilterActivity extends AppCompatActivity {

    DatePicker dpBeginDate;
    Spinner sSort;
    CheckBox cbArts;
    CheckBox cbFashionStyle;
    CheckBox cbSports;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        // Get the references to the views of the layout.
        setupViews();

        // Set the fields using the previous filter information (if any).
        Filter filter = (Filter) getIntent().getSerializableExtra("filter");
        setFields(filter);
    }

    /*
     * Get the reference for every view in the layout.
     */
    private void setupViews() {
        dpBeginDate = (DatePicker) findViewById(R.id.dpBeginDate);
        sSort = (Spinner) findViewById(R.id.sSort);
        cbArts = (CheckBox) findViewById(R.id.cbArts);
        cbFashionStyle = (CheckBox) findViewById(R.id.cbFashionStyle);
        cbSports = (CheckBox) findViewById(R.id.cbSpots);
        btnSave = (Button) findViewById(R.id.btnSave);
    }

    /*
     * Sets the filter information provided by the user into the fields. It also sets some default info.
     */
    private void setFields(Filter filter) {
        // Set the max date of the date picker to today's date.
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        dpBeginDate.setMaxDate(today.getTime());

        // If filter is null, set the default values. Otherwise use the filter data.
        if (filter == null) {
            dpBeginDate.updateDate(2000, 6, 25);

        } else {
            // Set date picker.
            c.setTime(filter.getBeginDate());
            dpBeginDate.updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

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

            if (filter.hasNewsDeskValue(Filter.NewsDeskValues.FASHIONSTYLE)) {
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

    /*
     * Event fired when the save button is clicked.
     */
    public void saveFilters(View view) {

        // Extract the filter from the input fields.
        Filter filter = getFiltersFromViews();

        // Create the data that will be returned to the SearchActivity.
        Intent data = new Intent();
        data.putExtra("filter", filter);
        setResult(RESULT_OK, data);

        // Closes the activity.
        finish();
    }

    /*
     * Returns a Filter object with the filter information provided by the user.
     */
    private Filter getFiltersFromViews() {
        // Get the begin date.
        Calendar c = Calendar.getInstance();
        c.set(dpBeginDate.getYear(), dpBeginDate.getMonth(), dpBeginDate.getDayOfMonth());
        Date date = c.getTime();

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
            newsDeskValuesSet.add(Filter.NewsDeskValues.FASHIONSTYLE);
        }
        if (cbSports.isChecked()) {
            newsDeskValuesSet.add(Filter.NewsDeskValues.SPORTS);
        }

        // Create the filter object.
        Filter filter = new Filter();
        filter.setBeginDate(date);
        filter.setSortOrder(sortOrder);
        filter.setNewsDeskValues(newsDeskValuesSet);

        return filter;
    }
}
