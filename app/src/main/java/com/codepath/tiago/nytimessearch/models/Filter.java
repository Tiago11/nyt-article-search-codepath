package com.codepath.tiago.nytimessearch.models;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by tiago on 9/21/17.
 */

public class Filter implements Serializable {
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
        FASHIONSTYLE,
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
        return this.beginDate.toString();
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
}
