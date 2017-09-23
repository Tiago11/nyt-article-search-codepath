package com.codepath.tiago.nytimessearch.models;

import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tiago on 9/21/17.
 */

@Parcel
public class Filter {

    // Tag for logging.
    final String TAG = Filter.class.toString();

    Date beginDate;
    SortValues sort;
    Set<NewsDeskValues> newsDeskValues;

    // Enum with the possible sort values.
    public enum SortValues {
        OLDEST,
        NEWEST;
    }

    // Enum with the possible news desk values.
    public enum NewsDeskValues {
        ARTS,
        FASHION_STYLE,
        SPORTS;
    }

    // Constructors.
    public Filter() {
        this.beginDate = null;
        this.sort = null;
        this.newsDeskValues = new HashSet<>();
    }

    // Getters.
    public String getBeginDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(this.beginDate);
    }

    public Date getBeginDate() {
        return this.beginDate;
    }

    public SortValues getSortOrder() {
        return this.sort;
    }

    // Setters.
    public void setBeginDate(Date date) {
        this.beginDate = date;
    }

    public void setSortOrder(SortValues sortOrder) {
        this.sort = sortOrder;
    }

    public void setNewsDeskValues(Set<NewsDeskValues> set) {
        this.newsDeskValues.addAll(set);
    }

    // Predicates.
    public boolean hasNewsDeskValue(NewsDeskValues newsDeskValue) {
        return (this.newsDeskValues.contains(newsDeskValue));
    }

    public int getNewsDeskValueSize() {
        return this.newsDeskValues.size();
    }
}
