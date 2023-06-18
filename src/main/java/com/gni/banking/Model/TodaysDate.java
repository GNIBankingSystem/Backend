package com.gni.banking.Model;

import java.util.Calendar;
import java.util.Date;

public class TodaysDate {

    public Date beginDate;
    public Date endDate;

    public TodaysDate() {
        this.beginDate = setStartDate();
        this.endDate = setEndDate();
    }
    Date setStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        calendar.add(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    Date setEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        calendar.add(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
