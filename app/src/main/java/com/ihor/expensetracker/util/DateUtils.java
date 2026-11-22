package com.ihor.expensetracker.util;

import java.util.Calendar;
import java.util.TimeZone;

public class DateUtils {

    public static long startOfMonth(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    public static long endOfMonth(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(startOfMonth(time));
        c.add(Calendar.MONTH, 1);
        return c.getTimeInMillis() - 1;
    }

    public static long fromPickerUtc(long utcMillis) {
        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utc.setTimeInMillis(utcMillis);
        Calendar local = Calendar.getInstance();
        local.clear();
        local.set(utc.get(Calendar.YEAR), utc.get(Calendar.MONTH),
                utc.get(Calendar.DAY_OF_MONTH), 12, 0, 0);
        return local.getTimeInMillis();
    }
}
