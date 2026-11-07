package com.ihor.expensetracker.util;

import com.ihor.expensetracker.data.Expense;

import java.math.BigDecimal;
import java.util.List;

public class MoneyUtils {

    public static long sum(List<Expense> expenses) {
        long total = 0;
        for (Expense e : expenses) {
            total += e.amount;
        }
        return total;
    }

    public static String format(long cents) {
        return BigDecimal.valueOf(cents, 2).toPlainString();
    }
}
