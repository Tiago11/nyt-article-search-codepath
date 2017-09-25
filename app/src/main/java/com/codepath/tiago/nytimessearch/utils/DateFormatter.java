package com.codepath.tiago.nytimessearch.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tiago on 9/24/17.
 */

public class DateFormatter {

    public DateFormatter() {

    }

    public static String FormatDateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return formatter.format(date);
    }
}
