package com.ihor.expensetracker.util;

import java.util.Calendar;

public class DateUtils {

    /** First millisecond of the month containing the given time. */
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

    /** Last millisecond of the month containing the given time. */
    public static long endOfMonth(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(startOfMonth(time));
        c.add(Calendar.MONTH, 1);
        return c.getTimeInMillis() - 1;
    }
}
